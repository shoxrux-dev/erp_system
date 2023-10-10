package com.example.erp_system.convert;

import com.example.erp_system.dto.outgoing_product_from_inventory.OutgoingProductFromInventoryResponseDto;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.model.OutgoingProductFromInventory;
import com.example.erp_system.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class OutgoingProductFromInventoryConverter {
    public List<OutgoingProductFromInventoryResponseDto> from(
            List<OutgoingProductFromInventory> outgoingProductFromInventories,
            List<Product> products,
            List<Inventory> inventories
    ) {
        List<OutgoingProductFromInventoryResponseDto> outgoingProductFromInventoryResponseDtos = new ArrayList<>();

        outgoingProductFromInventories.parallelStream().forEach(outgoingProductFromInventory ->
                inventories.forEach(inventory ->
                        products.parallelStream().forEach(product ->
                        {
                            if (
                                    outgoingProductFromInventory.getProductId()==product.getId() &&
                                            outgoingProductFromInventory.getInventoryId()==inventory.getId()
                            ) {
                                outgoingProductFromInventoryResponseDtos.add(
                                        OutgoingProductFromInventoryResponseDto.builder()
                                                .id(outgoingProductFromInventory.getId())
                                                .productId(outgoingProductFromInventory.getProductId())
                                                .productName(product.getName())
                                                .inventoryId(outgoingProductFromInventory.getInventoryId())
                                                .inventoryName(inventory.getName())
                                                .count(outgoingProductFromInventory.getCount())
                                                .createdAt(outgoingProductFromInventory.getCreatedAt())
                                                .updatedAt(outgoingProductFromInventory.getUpdatedAt())
                                                .build()
                                );
                            }
                        }
                        )));
        Collections.sort(outgoingProductFromInventoryResponseDtos);
        return outgoingProductFromInventoryResponseDtos;
    }
}
