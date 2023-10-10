package com.example.erp_system.service;

import com.example.erp_system.convert.IncomingProductToInventoryConverter;
import com.example.erp_system.dto.incoming_product_to_inventory.IncomingProductToInventoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.model.IncomingProductToInventory;
import com.example.erp_system.repository.IncomingProductToInventoryRepository;
import com.example.erp_system.repository.InventoryRepository;
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
public class IncomingProductToInventoryService {
    private final IncomingProductToInventoryRepository incomingProductToInventoryRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void create(IncomingProduct incomingProduct) {
        IncomingProductToInventory incomingProductToInventory = IncomingProductToInventory.builder()
                .productId(incomingProduct.getProductId())
                .inventoryId(incomingProduct.getInventoryId())
                .count(incomingProduct.getCount()).build();
        Instant now = Instant.now();
        incomingProductToInventory.setCreatedAt(now);
        incomingProductToInventory.setUpdatedAt(now);
        incomingProductToInventoryRepository.save(incomingProductToInventory);
    }

    @Transactional
    public void update(IncomingProduct oldIncomingProduct, IncomingProduct newIncomingProduct) {
        Instant start = oldIncomingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldIncomingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        IncomingProductToInventory byUpdatedAtBetween = getByUpdatedAtBetween(start, end);

        byUpdatedAtBetween.setProductId(newIncomingProduct.getProductId());
        byUpdatedAtBetween.setInventoryId(newIncomingProduct.getInventoryId());
        byUpdatedAtBetween.setCount(newIncomingProduct.getCount());
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        incomingProductToInventoryRepository.save(byUpdatedAtBetween);
    }

    public PageResponse<IncomingProductToInventoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<IncomingProductToInventory> incomingProductToInventories = incomingProductToInventoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                IncomingProductToInventoryConverter.from(incomingProductToInventories.getContent(), inventoryRepository.findAll(), productRepository.findAll()),
                incomingProductToInventories.getNumber(),
                incomingProductToInventories.getSize(),
                incomingProductToInventories.getTotalElements(),
                incomingProductToInventories.getTotalPages(),
                incomingProductToInventories.isLast()
        );
    }

    public IncomingProductToInventory get(int id) {
        return incomingProductToInventoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("incoming product to inventory not found with %s id", id)));
    }

    public IncomingProductToInventory getByUpdatedAtBetween(Instant start, Instant end) {
        return incomingProductToInventoryRepository.findIncomingProductToInventoryByUpdatedAtBetween(start, end)
                .orElseThrow(() -> new RecordNotFoundException(String.format("incoming product to inventory not found with %s start time and %s end time", start, end)));
    }

}
