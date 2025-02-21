package com.kiosk.server.banking.domain.mapper;

import com.kiosk.server.banking.application.service.banking.vo.WithdrawVO;
import com.kiosk.server.banking.domain.Withdraw;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.infra.rdb.entity.WithdrawEventEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {

    Withdraw toDomain(WithdrawVO.Save withDraw);
    WithdrawEventEntity toEntity(Withdraw withdraw);

    Withdraw toDomainFromEntity(WithdrawEventEntity entity);

    @AfterMapping
    default void setWithdrawId(WithdrawVO.Save withdrawVO, @MappingTarget Withdraw withdraw) {
        withdraw.setWithdrawEventId(IdUtil.create());
    }
}
