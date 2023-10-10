package com.example.erp_system.convert;

import com.example.erp_system.dto.incoming_product_to_inventory.IncomingProductToInventoryResponseDto;
import com.example.erp_system.model.IncomingProductToInventory;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class IncomingProductToInventoryConverter {
    public List<IncomingProductToInventoryResponseDto> from(
            List<IncomingProductToInventory> incomingProductToInventories,
            List<Inventory> inventories,
            List<Product> products
    ) {
        List<IncomingProductToInventoryResponseDto> incomingProductToInventoryResponseDtos = new ArrayList<>();

        incomingProductToInventories.parallelStream().forEach(incomingProductToInventory -> inventories.forEach(inventory -> products.parallelStream().forEach(product -> {
            if (
                    incomingProductToInventory.getProductId()==product.getId() &&
                    incomingProductToInventory.getInventoryId()==inventory.getId()
            ) {
                incomingProductToInventoryResponseDtos.add(
                        IncomingProductToInventoryResponseDto.builder()
                                .id(incomingProductToInventory.getId())
                                .productId(incomingProductToInventory.getProductId())
                                .productName(product.getName())
                                .inventoryId(incomingProductToInventory.getInventoryId())
                                .inventoryName(inventory.getName())
                                .count(incomingProductToInventory.getCount())
                                .createdAt(incomingProductToInventory.getCreatedAt())
                                .updatedAt(incomingProductToInventory.getUpdatedAt())
                                .build()
                );
            }
        })));
        Collections.sort(incomingProductToInventoryResponseDtos);
        return incomingProductToInventoryResponseDtos;
    }

}
