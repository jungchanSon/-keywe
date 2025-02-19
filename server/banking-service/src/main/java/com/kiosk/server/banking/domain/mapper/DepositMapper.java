package com.kiosk.server.banking.domain.mapper;

import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import com.kiosk.server.banking.domain.Deposit;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.infra.rdb.entity.DepositEventEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    Deposit toDomain(DepositVO.Save deposit);
    DepositEventEntity toEntity(Deposit deposit);

    Deposit toDomainEntity(DepositEventEntity entity);

    @AfterMapping
    default void setDepositId(DepositVO.Save depositVO, @MappingTarget Deposit deposit) {
        deposit.setDepositEventId(IdUtil.create());
    }

}
