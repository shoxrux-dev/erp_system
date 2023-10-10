package com.example.erp_system.service;

import com.example.erp_system.convert.ProductInInventoryConverter;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.dto.product_in_inventory.ProductInInventoryResponseDto;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.model.ProductInInventory;
import com.example.erp_system.repository.InventoryRepository;
import com.example.erp_system.repository.ProductInInventoryRepository;
import com.example.erp_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductInInventoryService {
    private final ProductInInventoryRepository productInInventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void add(IncomingProduct incomingProduct) {
        Optional<ProductInInventory> productInInventoryByProductIdAndInventoryId = productInInventoryRepository.findProductInInventoryByProductIdAndInventoryId(incomingProduct.getProductId(), incomingProduct.getInventoryId());

        ProductInInventory productInInventory;
        if(productInInventoryByProductIdAndInventoryId.isPresent()){
            productInInventory = productInInventoryByProductIdAndInventoryId.get();
            productInInventory.setCount(productInInventory.getCount() + incomingProduct.getCount());
            productInInventory.setUpdatedAt(Instant.now());
        }else{
            productInInventory = ProductInInventory.builder()
                    .productId(incomingProduct.getProductId())
                    .count(incomingProduct.getCount())
                    .inventoryId(incomingProduct.getInventoryId()).build();
            Instant now = Instant.now();
            productInInventory.setCreatedAt(now);
            productInInventory.setUpdatedAt(now);
        }
        productInInventoryRepository.save(productInInventory);
    }

    @Transactional
    public void update(IncomingProduct oldIncomingProduct, IncomingProduct newIncomingProduct) {
        ProductInInventory byProductIdAndInventoryId1 = getByProductIdAndInventoryId(oldIncomingProduct.getProductId(), oldIncomingProduct.getInventoryId());
        byProductIdAndInventoryId1.setInventoryId(newIncomingProduct.getInventoryId());
        byProductIdAndInventoryId1.setProductId(newIncomingProduct.getProductId());
        byProductIdAndInventoryId1.setCount(byProductIdAndInventoryId1.getCount() - oldIncomingProduct.getCount() + newIncomingProduct.getCount());
        byProductIdAndInventoryId1.setUpdatedAt(Instant.now());
        productInInventoryRepository.save(byProductIdAndInventoryId1);
    }

    @Transactional
    public void update(OutgoingProduct oldOutgoingProduct, OutgoingProduct newOutgoingProduct) {
        ProductInInventory byProductIdAndInventoryId1 = getByProductIdAndInventoryId(oldOutgoingProduct.getProductId(), oldOutgoingProduct.getInventoryId());
        byProductIdAndInventoryId1.setInventoryId(newOutgoingProduct.getInventoryId());
        byProductIdAndInventoryId1.setProductId(newOutgoingProduct.getProductId());
        byProductIdAndInventoryId1.setCount(byProductIdAndInventoryId1.getCount() + oldOutgoingProduct.getCount() - oldOutgoingProduct.getCount());
        byProductIdAndInventoryId1.setUpdatedAt(Instant.now());
        productInInventoryRepository.save(byProductIdAndInventoryId1);
    }

    public PageResponse<ProductInInventoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<ProductInInventory> productInInventories = productInInventoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                ProductInInventoryConverter.from(productInInventories.getContent(), inventoryRepository.findAll(),  productRepository.findAll()),
                productInInventories.getNumber(),
                productInInventories.getSize(),
                productInInventories.getTotalElements(),
                productInInventories.getTotalPages(),
                productInInventories.isLast()
        );
    }

    public ProductInInventory getByProductIdAndInventoryId(int productId, int inventoryId) {
        return productInInventoryRepository.findProductInInventoryByProductIdAndInventoryId(productId, inventoryId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("product to inventory not found with %s product id and %s inventory id", productId, inventoryId)));
    }

    protected void separate(OutgoingProduct outgoingProduct) {
        ProductInInventory byProductIdAndInventoryId = getByProductIdAndInventoryId(outgoingProduct.getProductId(), outgoingProduct.getInventoryId());
        byProductIdAndInventoryId.setCount(byProductIdAndInventoryId.getCount() - outgoingProduct.getCount());
        productInInventoryRepository.save(byProductIdAndInventoryId);
    }

}
