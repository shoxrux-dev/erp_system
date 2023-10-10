package com.example.erp_system.convert;

import com.example.erp_system.dto.product_in_inventory.ProductInInventoryResponseDto;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.model.Product;
import com.example.erp_system.model.ProductInInventory;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ProductInInventoryConverter {
    public List<ProductInInventoryResponseDto> from(
            List<ProductInInventory> productInInventories,
            List<Inventory> inventories,
            List<Product> products
    ) {
        List<ProductInInventoryResponseDto> productInInventoryResponseDtos = new ArrayList<>();
        productInInventories.forEach(productInInventory ->
                inventories.forEach(inventory ->
                        products.forEach(product ->
                        {
                            if (
                                    productInInventory.getProductId()==product.getId() &&
                                            productInInventory.getInventoryId()==inventory.getId()
                            ) {
                                productInInventoryResponseDtos.add(
                                        ProductInInventoryResponseDto.builder()
                                                .id(productInInventory.getId())
                                                .inventoryId(productInInventory.getInventoryId())
                                                .inventoryName(inventory.getName())
                                                .productId(productInInventory.getProductId())
                                                .productName(product.getName())
                                                .count(productInInventory.getCount())
                                                .createdAt(productInInventory.getCreatedAt())
                                                .updatedAt(productInInventory.getUpdatedAt())
                                                .build()
                                );
                            }
                        })));
        Collections.sort(productInInventoryResponseDtos);
        return productInInventoryResponseDtos;
    }
}
