//package com.kiosk.server.settlement.domain.mapper;
//
//import com.kiosk.server.infra.rdb.entity.SettlementEntity;
//import com.kiosk.server.common.util.IdUtil;
//import com.kiosk.server.settlement.application.service.settlement.vo.SettlementVO;
//import com.kiosk.server.settlement.domain.Settlement;
//import org.mapstruct.AfterMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.MappingTarget;
//
//@Mapper(componentModel = "spring")
//public interface SettlementMapper {
//
//    Settlement toDomain(SettlementVO.Save save);
//    SettlementEntity toEntity(Settlement settlement);
//
//    @AfterMapping
//    default void setAccountId(@MappingTarget Settlement settlement) {
//        settlement.setSettlementId(IdUtil.create());
//    }
//}
