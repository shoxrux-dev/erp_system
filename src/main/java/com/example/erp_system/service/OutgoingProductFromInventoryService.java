package com.example.erp_system.service;

import com.example.erp_system.convert.OutgoingProductFromInventoryConverter;
import com.example.erp_system.dto.outgoing_product_from_inventory.OutgoingProductFromInventoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.model.OutgoingProductFromInventory;
import com.example.erp_system.repository.InventoryRepository;
import com.example.erp_system.repository.OutgoingProductFromInventoryRepository;
import com.example.erp_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutgoingProductFromInventoryService {
    private final OutgoingProductFromInventoryRepository outgoingProductFromInventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void create(OutgoingProduct outgoingProduct) {
        OutgoingProductFromInventory outgoingProductFromInventory = OutgoingProductFromInventory.builder()
                .productId(outgoingProduct.getProductId())
                .inventoryId(outgoingProduct.getInventoryId())
                .count(outgoingProduct.getCount()).build();

        Instant now = Instant.now();
        outgoingProductFromInventory.setCreatedAt(now);
        outgoingProductFromInventory.setUpdatedAt(now);
        outgoingProductFromInventoryRepository.save(outgoingProductFromInventory);
    }

    public List<OutgoingProductFromInventory> getAll() {
        List<OutgoingProductFromInventory> productFromInventories = outgoingProductFromInventoryRepository.findAll();
        return productFromInventories;
    }

    public PageResponse<OutgoingProductFromInventoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<OutgoingProductFromInventory> outgoingProductFromInventories = outgoingProductFromInventoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                OutgoingProductFromInventoryConverter.from(outgoingProductFromInventories.getContent(), productRepository.findAll(), inventoryRepository.findAll()),
                outgoingProductFromInventories.getNumber(),
                outgoingProductFromInventories.getSize(),
                outgoingProductFromInventories.getTotalElements(),
                outgoingProductFromInventories.getTotalPages(),
                outgoingProductFromInventories.isLast()
        );
    }

    @Transactional
    public void update(OutgoingProduct oldOutgoingProduct,OutgoingProduct newOutgoingProduct) {
        Instant start = oldOutgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldOutgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        OutgoingProductFromInventory byUpdatedAtBetween = getByUpdatedAtBetween(start, end);
        byUpdatedAtBetween.setProductId(newOutgoingProduct.getProductId());
        byUpdatedAtBetween.setInventoryId(newOutgoingProduct.getInventoryId());
        byUpdatedAtBetween.setCount(newOutgoingProduct.getCount());
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        outgoingProductFromInventoryRepository.save(byUpdatedAtBetween);
    }


    public OutgoingProductFromInventory getByUpdatedAtBetween(Instant start, Instant end) {
        return outgoingProductFromInventoryRepository.findOutgoingProductFromInventoryByUpdatedAtBetween(start, end)
                .orElseThrow(() -> new RecordNotFoundException(String.format("outgoing product from inventory not found with %s start time and %s end time", start, end)));
    }


}
