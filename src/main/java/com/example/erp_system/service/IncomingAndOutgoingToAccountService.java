package com.example.erp_system.service;

import com.example.erp_system.constant.AccountStatusEnum;
import com.example.erp_system.convert.IncomingAndOutgoingToAccountConverter;
import com.example.erp_system.convert.ProductConvertor;
import com.example.erp_system.dto.incoming_and_outgoing_to_account.IncomingAndOutgoingToAccountResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.IncomingAndOutgoingToAccount;
import com.example.erp_system.model.IncomingProduct;
import com.example.erp_system.model.OutgoingProduct;
import com.example.erp_system.model.Product;
import com.example.erp_system.repository.CompanyRepository;
import com.example.erp_system.repository.IncomingAndOutGoingToAccountRepository;
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
public class IncomingAndOutgoingToAccountService {
    private final IncomingAndOutGoingToAccountRepository incomingAndOutGoingToAccountRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void create(IncomingProduct incomingProduct) {
        IncomingAndOutgoingToAccount incomingAndOutgoingToAccount = IncomingAndOutgoingToAccount.builder()
                .companyId(incomingProduct.getCompanyId())
                .price(incomingProduct.getPrice())
                .status(AccountStatusEnum.OUTED).build();
        Instant now = Instant.now();
        incomingAndOutgoingToAccount.setCreatedAt(now);
        incomingAndOutgoingToAccount.setUpdatedAt(now);
        incomingAndOutGoingToAccountRepository.save(incomingAndOutgoingToAccount);
    }

    @Transactional
    public void create(OutgoingProduct outgoingProduct) {
        IncomingAndOutgoingToAccount incomingAndOutgoingToAccount = IncomingAndOutgoingToAccount.builder()
                .companyId(outgoingProduct.getCompanyId())
                .price(outgoingProduct.getPrice())
                .status(AccountStatusEnum.ENTERED).build();
        Instant now = Instant.now();
        incomingAndOutgoingToAccount.setCreatedAt(now);
        incomingAndOutgoingToAccount.setUpdatedAt(now);
        incomingAndOutGoingToAccountRepository.save(incomingAndOutgoingToAccount);
    }


    @Transactional
    public void update(IncomingProduct oldIncomingProduct, IncomingProduct newIncomingProduct) {
        Instant start = oldIncomingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldIncomingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        IncomingAndOutgoingToAccount byUpdatedAtBetween = getByUpdatedAtBetween(start, end);

        byUpdatedAtBetween.setCompanyId(newIncomingProduct.getCompanyId());
        byUpdatedAtBetween.setPrice(newIncomingProduct.getPrice());
        byUpdatedAtBetween.setStatus(AccountStatusEnum.OUTED);
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        incomingAndOutGoingToAccountRepository.save(byUpdatedAtBetween);
    }

    @Transactional
    public void update(OutgoingProduct oldOutgoingProduct, OutgoingProduct newOutgoingProduct) {
        Instant start = oldOutgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = oldOutgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        IncomingAndOutgoingToAccount byUpdatedAtBetween = incomingAndOutGoingToAccountRepository.findIncomingAndOutgoingToAccountByUpdatedAtBetween(start, end).orElse(null);

        if(byUpdatedAtBetween == null) {
            create(newOutgoingProduct);
            return;
        }

        byUpdatedAtBetween.setCompanyId(newOutgoingProduct.getCompanyId());
        byUpdatedAtBetween.setPrice(newOutgoingProduct.getPrice());
        byUpdatedAtBetween.setStatus(AccountStatusEnum.ENTERED);
        byUpdatedAtBetween.setUpdatedAt(Instant.now());
        incomingAndOutGoingToAccountRepository.save(byUpdatedAtBetween);
    }

    public PageResponse<IncomingAndOutgoingToAccountResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<IncomingAndOutgoingToAccount> incomingAndOutgoingToAccounts = incomingAndOutGoingToAccountRepository.findAll(pageRequest);

        return new PageResponse<>(
                IncomingAndOutgoingToAccountConverter.from(incomingAndOutgoingToAccounts.getContent(), companyRepository.findAll()),
                incomingAndOutgoingToAccounts.getNumber(),
                incomingAndOutgoingToAccounts.getSize(),
                incomingAndOutgoingToAccounts.getTotalElements(),
                incomingAndOutgoingToAccounts.getTotalPages(),
                incomingAndOutgoingToAccounts.isLast()
        );
    }

    public IncomingAndOutgoingToAccount getByUpdatedAtBetween(Instant start, Instant end) {
        return incomingAndOutGoingToAccountRepository.findIncomingAndOutgoingToAccountByUpdatedAtBetween(start, end)
                .orElseThrow(() -> new RecordNotFoundException(String.format("incoming and outgoing to account not found with %s start time and %s end time", start, end)));
    }

    @Transactional
    public void delete(OutgoingProduct outgoingProduct) {
        Instant start = outgoingProduct.getUpdatedAt().minus(1, ChronoUnit.SECONDS);
        Instant end = outgoingProduct.getUpdatedAt().plus(1, ChronoUnit.SECONDS);

        IncomingAndOutgoingToAccount byUpdatedAtBetween = getByUpdatedAtBetween(start, end);
        incomingAndOutGoingToAccountRepository.delete(byUpdatedAtBetween);
    }

}
