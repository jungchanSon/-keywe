package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.inbound.AccountUseCase;
import com.kiosk.server.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.server.banking.application.port.outbound.AccountQueryPort;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.Deposit;
import com.kiosk.server.banking.domain.Withdraw;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.backoff.BackOff;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountCommandPort accountCommandPort;
    private final AccountQueryPort accountQueryPort;

    private final DepositReader depositReader;
    private final WithdrawReader withdrawReader;

    @Override
    @Transactional
    @Retryable(
        backoff = @Backoff(delay=300)
    )
    public Account getAccount(Long memberId) {
        Account account = accountQueryPort.getAccountByUserId(memberId);

        Long depositSnapshotId = account.getDepositSnapshotId();
        Long withdrawSnapshotId = account.getWithdrawSnapshotId();

        List<Deposit> deposits = depositReader.searchDepositList(memberId, depositSnapshotId);
        List<Withdraw> withdraws = withdrawReader.searchDepositList(memberId, withdrawSnapshotId);

        Account currentAccount = getCurrentAccountStat(account, deposits, withdraws);

        saveCurrentAccountSnapshot(currentAccount);

        return account;
    }

    private Account getCurrentAccountStat(Account account, List<Deposit> deposits, List<Withdraw> withdraws) {

        Long currentBalance = account.getBalance();
        Long currentDepositAmount = deposits.stream().mapToLong(Deposit::getAmount).sum();;
        Long currentWithdrawAmount = withdraws.stream().mapToLong(Withdraw::getAmount).sum();

        Long depositSnapshotId = deposits.isEmpty() ? 0L : deposits.get(deposits.size()-1).getDepositEventId();
        Long withdrawSnapshotId = withdraws.isEmpty() ? 0L : withdraws.get(withdraws.size()-1).getWithdrawEventId();

        account.setBalance(currentBalance + currentDepositAmount - currentWithdrawAmount);
        account.setDepositSnapshotId(depositSnapshotId);
        account.setWithdrawSnapshotId(withdrawSnapshotId);

        return account;
    }


    private void saveCurrentAccountSnapshot(Account currentAccount) {
        accountCommandPort.update(currentAccount);
    }
}
