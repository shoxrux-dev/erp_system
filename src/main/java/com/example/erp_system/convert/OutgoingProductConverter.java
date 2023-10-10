package com.example.erp_system.convert;

import com.example.erp_system.dto.outgoing_product.BaseOutgoingProductRequestDto;
import com.example.erp_system.dto.outgoing_product.OutgoingProductResponseDto;
import com.example.erp_system.model.Company;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class OutgoingProductConverter {
    public OutgoingProduct convertToDomain(BaseOutgoingProductRequestDto baseOutgoingProductRequestDto) {
        OutgoingProduct outgoingProduct = new OutgoingProduct();
        outgoingProduct.setProductId(baseOutgoingProductRequestDto.getProductId());
        outgoingProduct.setCompanyId(baseOutgoingProductRequestDto.getCompanyId());
        outgoingProduct.setCount(baseOutgoingProductRequestDto.getCount());
        outgoingProduct.setPrice(baseOutgoingProductRequestDto.getPrice());
        outgoingProduct.setInventoryId(baseOutgoingProductRequestDto.getInventoryId());
        outgoingProduct.setStatus(baseOutgoingProductRequestDto.getStatus());
        return outgoingProduct;
    }

    public OutgoingProductResponseDto from(
            OutgoingProduct outgoingProduct,
            Product product,
            Company company,
            Inventory inventory
    ) {
        return OutgoingProductResponseDto.builder()
                .id(outgoingProduct.getId())
                .productId(product.getId())
                .productName(product.getName())
                .status(outgoingProduct.getStatus())
                .price(outgoingProduct.getPrice())
                .count(outgoingProduct.getCount())
                .inventoryId(inventory.getId())
                .inventoryName(inventory.getName())
                .companyId(company.getId())
                .companyName(company.getName())
                .createdAt(outgoingProduct.getCreatedAt())
                .updatedAt(outgoingProduct.getUpdatedAt())
                .build();
    }

    public List<OutgoingProductResponseDto> from(
            List<OutgoingProduct> outgoingProducts,
            List<Product> products,
            List<Company> companies,
            List<Inventory> inventories
    ) {
        List<OutgoingProductResponseDto> outgoingProductResponseDtos = new ArrayList<>();

        outgoingProducts.parallelStream().forEach(outgoingProduct ->
                products.parallelStream().forEach(product ->
                        companies.forEach(company ->
                                inventories.forEach(inventory ->
                                {
                                    if (
                                            outgoingProduct.getProductId()==product.getId() &&
                                                    outgoingProduct.getCompanyId()==company.getId() &&
                                                    outgoingProduct.getInventoryId()==inventory.getId()
                                    ) {
                                        outgoingProductResponseDtos.add(
                                                OutgoingProductResponseDto.builder()
                                                        .id(outgoingProduct.getId())
                                                        .status(outgoingProduct.getStatus())
                                                        .productId(outgoingProduct.getProductId())
                                                        .productName(product.getName())
                                                        .companyId(outgoingProduct.getCompanyId())
                                                        .companyName(company.getName())
                                                        .count(outgoingProduct.getCount())
                                                        .price(outgoingProduct.getPrice())
                                                        .inventoryId(outgoingProduct.getInventoryId())
                                                        .inventoryName(inventory.getName())
                                                        .createdAt(outgoingProduct.getCreatedAt())
                                                        .updatedAt(outgoingProduct.getUpdatedAt())
                                                        .build()
                                        );
                                    }
                                }))));
        Collections.sort(outgoingProductResponseDtos);
        return outgoingProductResponseDtos;
    }

}
