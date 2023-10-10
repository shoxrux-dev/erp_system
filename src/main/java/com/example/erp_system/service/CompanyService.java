package com.example.erp_system.service;


import com.example.erp_system.convert.CompanyConverter;
import com.example.erp_system.dto.company.CompanyResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Company;
import com.example.erp_system.repository.CompanyRepository;
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
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CommonSchemaValidator commonSchemaValidator;

    @Transactional
    public void create(Company company) {
        commonSchemaValidator.companyNotExist(company);
        Instant now = Instant.now();
        company.setCreatedAt(now);
        company.setUpdatedAt(now);
        companyRepository.save(company);
    }

    public PageResponse<CompanyResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Company> companies = companyRepository.findAll(pageRequest);

        return new PageResponse<>(
                CompanyConverter.from(companies.getContent()),
                companies.getNumber(),
                companies.getSize(),
                companies.getTotalElements(),
                companies.getTotalPages(),
                companies.isLast()
        );

    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company get(int id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("company not found with %s id", id)));
    }

    @Transactional
    public void update(int id, Company company) {
        Company company1 = get(id);
        company1.setName(company.getName());
        company1.setUpdatedAt(Instant.now());
        companyRepository.save(company1);
    }

    @Transactional
    public void delete(int id) {
        Company company = get(id);
        companyRepository.delete(company);
    }

}
