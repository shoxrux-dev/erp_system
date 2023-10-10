package com.example.erp_system.service;

import com.example.erp_system.constant.DebtStatusEnum;
import com.example.erp_system.convert.OutgoingProductConverter;
import com.example.erp_system.dto.outgoing_product.OutgoingProductResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.repository.CompanyRepository;
import com.example.erp_system.repository.InventoryRepository;
import com.example.erp_system.repository.OutgoingProductRepository;
import com.example.erp_system.repository.ProductRepository;
import com.example.erp_system.validation.CommonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutgoingProductService {
    private final OutgoingProductRepository outgoingProductRepository;
    private final ProductInInventoryService productInInventoryService;
    private final DebtService debtService;
    private final SalesService salesService;
    private final AccountService accountService;
    private final IncomingAndOutgoingToAccountService incomingAndOutgoingToAccountService;
    private final OutgoingProductFromInventoryService outgoingProductFromInventoryService;
    private final CommonSchemaValidator commonSchemaValidator;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void create(OutgoingProduct outgoingProduct) {
        commonSchemaValidator.validateOutgoingProduct(outgoingProduct);

        Instant now = Instant.now();
        outgoingProduct.setCreatedAt(now);
        outgoingProduct.setUpdatedAt(now);

        outgoingProductFromInventoryService.create(outgoingProduct);
        productInInventoryService.separate(outgoingProduct);
        debtService.create(outgoingProduct);

        if(outgoingProduct.getStatus().equals(DebtStatusEnum.NOT_DEBT)) {
            salesService.create(outgoingProduct);
            accountService.add(outgoingProduct);
            incomingAndOutgoingToAccountService.create(outgoingProduct);
        }

        outgoingProductRepository.save(outgoingProduct);
    }

    public PageResponse<OutgoingProductResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<OutgoingProduct> outgoingProducts = outgoingProductRepository.findAll(pageRequest);

        return new PageResponse<>(
                OutgoingProductConverter.from(outgoingProducts.getContent(), productRepository.findAll(), companyRepository.findAll(), inventoryRepository.findAll()),
                outgoingProducts.getNumber(),
                outgoingProducts.getSize(),
                outgoingProducts.getTotalElements(),
                outgoingProducts.getTotalPages(),
                outgoingProducts.isLast()
        );
    }

    @Transactional
    public void update(int id, OutgoingProduct newOutgoingProduct) {
        commonSchemaValidator.validateOutgoingProduct(newOutgoingProduct);

        OutgoingProduct oldOutgoingProduct = get(id);

        outgoingProductFromInventoryService.update(oldOutgoingProduct, newOutgoingProduct);
        productInInventoryService.update(oldOutgoingProduct, newOutgoingProduct);
        debtService.update(oldOutgoingProduct, newOutgoingProduct);

        if(newOutgoingProduct.getStatus().equals(DebtStatusEnum.NOT_DEBT)) {
            salesService.update(oldOutgoingProduct, newOutgoingProduct);
            accountService.update(oldOutgoingProduct,newOutgoingProduct);
            incomingAndOutgoingToAccountService.update(oldOutgoingProduct, newOutgoingProduct);
        }else{
            salesService.delete(oldOutgoingProduct);
            accountService.update(oldOutgoingProduct);
            incomingAndOutgoingToAccountService.delete(oldOutgoingProduct);
        }

        oldOutgoingProduct.setProductId(newOutgoingProduct.getProductId());
        oldOutgoingProduct.setInventoryId(newOutgoingProduct.getInventoryId());
        oldOutgoingProduct.setStatus(newOutgoingProduct.getStatus());
        oldOutgoingProduct.setCompanyId(newOutgoingProduct.getCompanyId());
        oldOutgoingProduct.setCount(newOutgoingProduct.getCount());
        oldOutgoingProduct.setPrice(newOutgoingProduct.getPrice());
        oldOutgoingProduct.setUpdatedAt(Instant.now());

        outgoingProductRepository.save(oldOutgoingProduct);
    }

    public OutgoingProduct get(int id) {
        return outgoingProductRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("outgoing product not found with %s id", id)));
    }

}
