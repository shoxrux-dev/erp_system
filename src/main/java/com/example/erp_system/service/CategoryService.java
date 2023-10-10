package com.example.erp_system.service;

import com.example.erp_system.convert.CategoryConverter;
import com.example.erp_system.dto.category.CategoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Category;
import com.example.erp_system.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CommonSchemaValidator commonSchemaValidator;

    @Transactional
    public void create(Category category) {
        commonSchemaValidator.categoryNotExist(category);
        Instant now = Instant.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        categoryRepository.save(category);
    }

    public PageResponse<CategoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                CategoryConverter.from(categories.getContent()),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast()
        );

    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category get(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("category not found with %s id", id)));
    }

    @Transactional
    public void update(int id, Category category) {
        Category category1 = get(id);
        category1.setName(category.getName());
        category1.setUpdatedAt(Instant.now());
        categoryRepository.save(category1);
    }

    @Transactional
    public void delete(int id) {
        Category category = get(id);
        categoryRepository.delete(category);
    }

}
