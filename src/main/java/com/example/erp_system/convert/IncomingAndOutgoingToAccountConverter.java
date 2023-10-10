package com.example.erp_system.convert;

import com.example.erp_system.dto.incoming_and_outgoing_to_account.IncomingAndOutgoingToAccountResponseDto;
import com.example.erp_system.model.Company;
import com.example.erp_system.model.IncomingAndOutgoingToAccount;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class IncomingAndOutgoingToAccountConverter {
    public List<IncomingAndOutgoingToAccountResponseDto> from(
            List<IncomingAndOutgoingToAccount> incomingAndOutgoingToAccounts,
            List<Company> companies
    ) {
        List<IncomingAndOutgoingToAccountResponseDto> incomingProductToInventoryResponseDtos = new ArrayList<>();

        incomingAndOutgoingToAccounts.forEach(incomingAndOutgoingToAccount -> companies.forEach(company -> {
                if(incomingAndOutgoingToAccount.getCompanyId()==company.getId()) {
                    incomingProductToInventoryResponseDtos.add(
                            IncomingAndOutgoingToAccountResponseDto.builder()
                                    .id(incomingAndOutgoingToAccount.getId())
                                    .price(incomingAndOutgoingToAccount.getPrice())
                                    .companyId(incomingAndOutgoingToAccount.getCompanyId())
                                    .companyName(company.getName())
                                    .status(incomingAndOutgoingToAccount.getStatus())
                                    .createdAt(incomingAndOutgoingToAccount.getCreatedAt())
                                    .updatedAt(incomingAndOutgoingToAccount.getUpdatedAt())
                                    .build()
                    );
                }
            }));

        Collections.sort(incomingProductToInventoryResponseDtos);

        return incomingProductToInventoryResponseDtos;
    }
}
