package com.kiosk.server.banking.adaptor.inbound.api.mapper;

import com.kiosk.server.banking.adaptor.inbound.api.dto.DepositRequestDTO;
import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositDTOMapper {

    DepositVO.Save toVO(DepositRequestDTO.Deposit deposit);
}
