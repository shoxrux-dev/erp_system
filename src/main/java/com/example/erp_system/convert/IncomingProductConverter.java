package com.example.erp_system.convert;

import com.example.erp_system.dto.incoming_product.BaseIncomingProductRequestDto;
import com.example.erp_system.dto.incoming_product.IncomingProductResponseDto;
import com.example.erp_system.model.Company;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class IncomingProductConverter {
    public IncomingProduct convertToDomain(BaseIncomingProductRequestDto baseIncomingProductRequestDto) {
        IncomingProduct incomingProduct = new IncomingProduct();
        incomingProduct.setProductId(baseIncomingProductRequestDto.getProductId());
        incomingProduct.setCompanyId(baseIncomingProductRequestDto.getCompanyId());
        incomingProduct.setCount(baseIncomingProductRequestDto.getCount());
        incomingProduct.setPrice(baseIncomingProductRequestDto.getPrice());
        incomingProduct.setInventoryId(baseIncomingProductRequestDto.getInventoryId());
        return incomingProduct;
    }

    public IncomingProductResponseDto from(
            IncomingProduct incomingProduct,
            Product product,
            Company company,
            Inventory inventory
    ){
        return IncomingProductResponseDto.builder()
                .id(incomingProduct.getId())
                .productId(product.getId())
                .productName(product.getName())
                .companyId(company.getId())
                .companyName(company.getName())
                .count(incomingProduct.getCount())
                .price(incomingProduct.getPrice())
                .inventoryId(inventory.getId())
                .inventoryName(inventory.getName())
                .createdAt(incomingProduct.getCreatedAt())
                .updatedAt(incomingProduct.getUpdatedAt())
                .build();
    }

    public List<IncomingProductResponseDto> from(
            List<IncomingProduct> incomingProducts,
            List<Product> products,
            List<Company> companies,
            List<Inventory> inventories
    ) {
        List<IncomingProductResponseDto> incomingProductResponseDtos = new ArrayList<>();

        incomingProducts.parallelStream().forEach(incomingProduct -> products.parallelStream().forEach(product -> companies.forEach(company -> inventories.forEach(inventory -> {
            if(
                    incomingProduct.getProductId()==product.getId() &&
                    incomingProduct.getCompanyId()==company.getId() &&
                    incomingProduct.getInventoryId()==inventory.getId()
            ) {
                incomingProductResponseDtos.add(
                        IncomingProductResponseDto.builder()
                                .id(incomingProduct.getId())
                                .productId(incomingProduct.getProductId())
                                .productName(product.getName())
                                .companyId(incomingProduct.getCompanyId())
                                .companyName(company.getName())
                                .count(incomingProduct.getCount())
                                .price(incomingProduct.getPrice())
                                .inventoryId(incomingProduct.getInventoryId())
                                .inventoryName(inventory.getName())
                                .createdAt(incomingProduct.getCreatedAt())
                                .updatedAt(incomingProduct.getUpdatedAt())
                                .build()
                );
            }
        }))));

        Collections.sort(incomingProductResponseDtos);

        return incomingProductResponseDtos;
    }

}
