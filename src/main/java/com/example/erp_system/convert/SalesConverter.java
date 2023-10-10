package com.example.erp_system.convert;

import com.example.erp_system.dto.sales.SalesResponseDto;
import com.example.erp_system.model.Company;
import com.example.erp_system.model.Product;
import com.example.erp_system.model.Sales;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class SalesConverter {
    public List<SalesResponseDto> from(
            List<Sales> salesList,
            List<Product> productList,
            List<Company> companyList
    ) {
        List<SalesResponseDto> salesResponseDtos = new ArrayList<>();

        salesList.parallelStream().forEach(sales ->
                productList.forEach(product ->
                        companyList.forEach(company ->
                        {
                            if (sales.getCompanyId()==company.getId() && sales.getProductId()==product.getId()) {
                                salesResponseDtos.add(
                                        SalesResponseDto.builder()
                                                .id(sales.getId())
                                                .revenue(sales.getRevenue())
                                                .companyId(company.getId())
                                                .companyName(company.getName())
                                                .productId(product.getId())
                                                .productName(product.getName())
                                                .count(sales.getCount())
                                                .createdAt(sales.getCreatedAt())
                                                .updatedAt(sales.getUpdatedAt())
                                                .build()
                                );
                            }
                        })));
        Collections.sort(salesResponseDtos);
        return salesResponseDtos;
    }
}
