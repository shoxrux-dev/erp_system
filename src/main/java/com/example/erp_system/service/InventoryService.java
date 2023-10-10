package com.example.erp_system.service;

import com.example.erp_system.convert.InventoryConverter;
import com.example.erp_system.dto.inventory.InventoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.repository.InventoryRepository;
import com.example.erp_system.validation.CommonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final CommonSchemaValidator commonSchemaValidator;

    @Transactional
    public void create(Inventory inventory) {
        commonSchemaValidator.inventoryNotExist(inventory);
        Instant now = Instant.now();
        inventory.setCreatedAt(now);
        inventory.setUpdatedAt(now);
        inventoryRepository.save(inventory);
    }

    public PageResponse<InventoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Inventory> inventories = inventoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                InventoryConverter.from(inventories.getContent()),
                inventories.getNumber(),
                inventories.getSize(),
                inventories.getTotalElements(),
                inventories.getTotalPages(),
                inventories.isLast()
        );

    }

    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    public Inventory get(int id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("inventory not found with %s id", id)));
    }

    @Transactional
    public void update(int id, Inventory inventory) {
        Inventory inventory1 = get(id);
        inventory1.setName(inventory.getName());
        inventory1.setUpdatedAt(Instant.now());
        inventoryRepository.save(inventory1);
    }

    @Transactional
    public void delete(int id) {
        Inventory inventory = get(id);
        inventoryRepository.delete(inventory);
    }
}
