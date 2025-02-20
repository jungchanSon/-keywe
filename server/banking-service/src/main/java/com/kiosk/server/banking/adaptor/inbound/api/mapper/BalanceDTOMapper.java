package com.kiosk.server.banking.adaptor.inbound.api.mapper;

import com.kiosk.server.banking.adaptor.inbound.api.dto.BalanceRequestDTO;
import com.kiosk.server.banking.application.service.banking.vo.BalanceHistoryVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceDTOMapper {

    BalanceHistoryVO.Save toVO(BalanceRequestDTO request);
}
