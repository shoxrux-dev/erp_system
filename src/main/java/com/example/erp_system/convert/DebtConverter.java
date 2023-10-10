package com.example.erp_system.convert;

import com.example.erp_system.dto.debt.DebtResponseDto;
import com.example.erp_system.model.Company;
import com.example.erp_system.model.Debt;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class DebtConverter {
    public List<DebtResponseDto> from(
            List<Debt> debtList,
            List<Company> companies
    ) {
        List<DebtResponseDto> debtResponseDtos = new ArrayList<>();

        debtList.parallelStream().forEach(debt -> companies.forEach(company -> {
                if(debt.getCompanyId() == company.getId()) {
                    debtResponseDtos.add(
                         DebtResponseDto.builder()
                                 .id(debt.getId())
                                 .companyName(company.getName())
                                 .companyId(company.getId())
                                 .amount(debt.getAmount())
                                 .status(debt.getStatus())
                                 .createdAt(debt.getCreatedAt())
                                 .updatedAt(debt.getUpdatedAt())
                                 .build()
                    );
                }
            })
        );

        Collections.sort(debtResponseDtos);
        return debtResponseDtos;
    }
}
