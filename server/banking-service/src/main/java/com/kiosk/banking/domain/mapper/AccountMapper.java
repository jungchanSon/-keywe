package com.kiosk.banking.domain.mapper;

import com.kiosk.banking.application.service.banking.vo.AccountVO;
import com.kiosk.banking.domain.Account;
import com.kiosk.infra.rdb.entity.AccountEntity;
import com.kiosk.server.common.util.IdUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toDomain(AccountVO.Save save);
    AccountEntity toEntity(Account account);

    @AfterMapping
    default void setAccountId(@MappingTarget Account account) {
        account.setBalance(100_000L);
        account.setAccountId(IdUtil.create());
    }
}
