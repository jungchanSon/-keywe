package com.kiosk.server.banking.adaptor.inbound.api.mapper;

import com.kiosk.server.banking.adaptor.inbound.api.dto.WithdrawRequestDTO;
import com.kiosk.server.banking.application.service.banking.vo.WithdrawVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawDTOMapper {

    WithdrawVO.Save toVO(WithdrawRequestDTO.Withdraw withdraw);
}
