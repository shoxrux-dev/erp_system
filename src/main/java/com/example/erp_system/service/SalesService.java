package com.example.erp_system.service;

import com.example.erp_system.convert.SalesConverter;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.dto.sales.SalesResponseDto;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.model.Sales;
import com.example.erp_system.repository.CompanyRepository;
import com.example.erp_system.repository.ProductRepository;
import com.example.erp_system.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final SalesRepository salesRepository;
    private final IncomingProductService incomingProductService;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void create(OutgoingProduct outgoingProduct) {

        Sales sales = Sales.builder()
                .count(outgoingProduct.getCount())
                .companyId(outgoingProduct.getCompanyId())
                .productId(outgoingProduct.getProductId())
                .revenue(
                        getRevenueOfNewAndLastProductPrice(outgoingProduct)
                ).build();

        Instant now = Instant.now();
        sales.setCreatedAt(now);
        sales.setUpdatedAt(now);
        salesRepository.save(sales);
    }

    public PageResponse<SalesResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Sales> sales = salesRepository.findAll(pageRequest);
        return new PageResponse<>(
                SalesConverter.from(sales.getContent(), productRepository.findAll(), companyRepository.findAll()),
                sales.getNumber(),
                sales.getSize(),
                sales.getTotalElements(),
                sales.getTotalPages(),
                sales.isLast()
        );
    }

    @Transactional
    public void update(OutgoingProduct oldOutgoingProduct, OutgoingProduct newOutgoingProduct) {
        Instant start = oldOutgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldOutgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        Sales byUpdatedAtBetween = salesRepository.findByUpdatedAtBetween(start, end).orElse(null);

        if(byUpdatedAtBetween == null) {
            create(newOutgoingProduct);
            return;
        }

        byUpdatedAtBetween.setCount(newOutgoingProduct.getCount());
        byUpdatedAtBetween.setProductId(newOutgoingProduct.getProductId());
        byUpdatedAtBetween.setCompanyId(newOutgoingProduct.getCompanyId());
        byUpdatedAtBetween.setRevenue(
                getRevenueOfNewAndLastProductPrice(newOutgoingProduct)
        );
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        salesRepository.save(byUpdatedAtBetween);
    }

    public BigDecimal getRevenueOfNewAndLastProductPrice(OutgoingProduct outgoingProduct) {
        IncomingProduct lastIncomingProduct = incomingProductService.getLastByProductId(outgoingProduct.getProductId());

        BigDecimal priceOfOneIncomingProduct = lastIncomingProduct.getPrice().divide(BigDecimal.valueOf(lastIncomingProduct.getCount()), RoundingMode.HALF_DOWN);
        BigDecimal priceOfOneOutgoingProduct = outgoingProduct.getPrice().divide(BigDecimal.valueOf(outgoingProduct.getCount()), RoundingMode.HALF_DOWN);

        return priceOfOneOutgoingProduct.subtract(priceOfOneIncomingProduct).multiply(BigDecimal.valueOf(outgoingProduct.getCount()));
    }

    @Transactional
    public void delete(OutgoingProduct outgoingProduct) {
        Instant start = outgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = outgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        salesRepository.findByUpdatedAtBetween(start, end).ifPresent(salesRepository::delete);

    }

}
