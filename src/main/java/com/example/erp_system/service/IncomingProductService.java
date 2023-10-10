package com.example.erp_system.service;

import com.example.erp_system.convert.IncomingProductConverter;
import com.example.erp_system.convert.ProductConvertor;
import com.example.erp_system.dto.incoming_product.IncomingProductResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.repository.CompanyRepository;
import com.example.erp_system.repository.IncomingProductRepository;
import com.example.erp_system.repository.InventoryRepository;
import com.example.erp_system.repository.ProductRepository;
import com.example.erp_system.validation.CommonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomingProductService {
    private final IncomingProductRepository incomingProductRepository;
    private final ProductInInventoryService productInInventoryService;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final AccountService accountService;
    private final IncomingAndOutgoingToAccountService incomingAndOutgoingToAccountService;
    private final IncomingProductToInventoryService incomingProductToInventoryService;
    private final CommonSchemaValidator commonSchemaValidator;

    @Transactional
    public void create(IncomingProduct incomingProduct) {
        commonSchemaValidator.validateIncomingProduct(incomingProduct);

        Instant now = Instant.now();
        incomingProduct.setCreatedAt(now);
        incomingProduct.setUpdatedAt(now);

        accountService.subtraction(incomingProduct);
        incomingProductToInventoryService.create(incomingProduct);
        productInInventoryService.add(incomingProduct);
        incomingAndOutgoingToAccountService.create(incomingProduct);

        incomingProductRepository.save(incomingProduct);
    }

    public PageResponse<IncomingProductResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<IncomingProduct> incomingProducts = incomingProductRepository.findAll(pageRequest);

        return new PageResponse<>(
                IncomingProductConverter.from(incomingProducts.getContent(), productRepository.findAll(), companyRepository.findAll(), inventoryRepository.findAll()),
                incomingProducts.getNumber(),
                incomingProducts.getSize(),
                incomingProducts.getTotalElements(),
                incomingProducts.getTotalPages(),
                incomingProducts.isLast()
        );
    }

    public IncomingProduct get(int id) {
        return incomingProductRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("incoming product not found with %s id", id)));
    }

    @Transactional
    public void update(int id, IncomingProduct newIncomingProduct) {
        commonSchemaValidator.validateIncomingProduct(newIncomingProduct);

        IncomingProduct oldIncomingProduct = get(id);

        productInInventoryService.update(oldIncomingProduct, newIncomingProduct);
        incomingProductToInventoryService.update(oldIncomingProduct, newIncomingProduct);
        accountService.update(oldIncomingProduct, newIncomingProduct);
        incomingAndOutgoingToAccountService.update(oldIncomingProduct, newIncomingProduct);

        oldIncomingProduct.setProductId(newIncomingProduct.getProductId());
        oldIncomingProduct.setCompanyId(newIncomingProduct.getCompanyId());
        oldIncomingProduct.setInventoryId(newIncomingProduct.getInventoryId());
        oldIncomingProduct.setCount(newIncomingProduct.getCount());
        oldIncomingProduct.setPrice(newIncomingProduct.getPrice());
        oldIncomingProduct.setUpdatedAt(Instant.now());

        incomingProductRepository.save(oldIncomingProduct);
    }

    public IncomingProduct getLastByProductId(int productId) {
        Optional<List<IncomingProduct>> incomingProductsByProductId = incomingProductRepository.findIncomingProductsByProductId(productId);
        if(incomingProductsByProductId.isEmpty()) {
            throw new RecordNotFoundException("no incoming product list found");
        }
        return incomingProductsByProductId.get().get(0);
    }

}
