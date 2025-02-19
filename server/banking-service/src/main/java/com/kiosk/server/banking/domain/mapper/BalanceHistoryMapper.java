package com.kiosk.server.banking.domain.mapper;

import com.kiosk.server.banking.application.service.banking.vo.BalanceHistoryVO;
import com.kiosk.server.banking.domain.BalanceHistory;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.infra.rdb.entity.BalanceHistoryEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BalanceHistoryMapper {

    BalanceHistory toDomain(BalanceHistoryVO.Save balance);
    BalanceHistoryEntity toEntity(BalanceHistory balanceHistory);

    @AfterMapping
    default void setBalanceHistoryId(@MappingTarget BalanceHistory balanceHistory) {
        balanceHistory.setBalanceHistoryId(IdUtil.create());
    }
}
