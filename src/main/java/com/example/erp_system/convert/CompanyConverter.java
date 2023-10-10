package com.example.erp_system.convert;

import com.example.erp_system.dto.company.BaseCompanyRequestDto;
import com.example.erp_system.dto.company.CompanyResponseDto;
import com.example.erp_system.model.Company;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class CompanyConverter {
    public <T extends BaseCompanyRequestDto> Company convertToDomain(T request) {
        return Company.builder()
                .name(request.getName())
                .build();
    }

    public CompanyResponseDto from(Company company){
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public List<CompanyResponseDto> from(List<Company> companies) {
        List<CompanyResponseDto> companyResponseDtos = new ArrayList<>();

        companies.forEach((company -> companyResponseDtos.add(
                    CompanyResponseDto.builder()
                            .id(company.getId())
                            .name(company.getName())
                            .createdAt(company.getCreatedAt())
                            .updatedAt(company.getUpdatedAt())
                            .build()
            )));

        Collections.sort(companyResponseDtos);

        return companyResponseDtos;
    }

}
