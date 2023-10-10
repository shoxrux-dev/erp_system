package com.example.erp_system.service;

import com.example.erp_system.convert.DebtConverter;
import com.example.erp_system.dto.debt.DebtResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Debt;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.repository.CompanyRepository;
import com.example.erp_system.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final DebtRepository debtRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void create(OutgoingProduct outgoingProduct) {
        Debt debt = Debt.builder()
                .companyId(outgoingProduct.getCompanyId())
                .status(outgoingProduct.getStatus())
                .amount(outgoingProduct.getPrice()).build();
        Instant now = Instant.now();
        debt.setCreatedAt(now);
        debt.setUpdatedAt(now);
        debtRepository.save(debt);
    }

    public PageResponse<DebtResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Debt> debts = debtRepository.findAll(pageRequest);
        return new PageResponse<>(
                DebtConverter.from(debts.getContent(), companyRepository.findAll()),
                debts.getNumber(),
                debts.getSize(),
                debts.getTotalElements(),
                debts.getTotalPages(),
                debts.isLast()
        );
    }

    @Transactional
    public void update(OutgoingProduct oldOutgoingProduct, OutgoingProduct newOutgoingProduct) {
        Instant start = oldOutgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldOutgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        Debt byUpdatedAtBetween = getByUpdatedAtBetween(start, end);

        byUpdatedAtBetween.setAmount(newOutgoingProduct.getPrice());
        byUpdatedAtBetween.setStatus(newOutgoingProduct.getStatus());
        byUpdatedAtBetween.setCompanyId(newOutgoingProduct.getCompanyId());
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        debtRepository.save(byUpdatedAtBetween);
    }

    public Debt getByUpdatedAtBetween(Instant start, Instant end) {
        return debtRepository.findDebtByUpdatedAtBetween(start, end)
                .orElseThrow(() -> new RecordNotFoundException(String.format("debt not found with %s start time and %s end time", start, end)));
    }

}
