package com.example.erp_system.service;

import com.example.erp_system.constant.FilePathConstant;
import com.example.erp_system.convert.ProductConvertor;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.dto.product.ProductResponseDto;
import com.example.erp_system.exception.FileDeletionException;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Product;
import com.example.erp_system.repository.CategoryRepository;
import com.example.erp_system.repository.ProductRepository;
import com.example.erp_system.util.FileUtil;
import com.example.erp_system.validation.CommonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CommonSchemaValidator commonSchemaValidator;

    @Transactional
    public void create(Product product, MultipartFile file) {
        commonSchemaValidator.productNotExist(product);
        commonSchemaValidator.categoryExist(product.getCategoryId());
        if(file != null && !file.isEmpty()){
            String fileName = FileUtil.uploadImage(file);
            product.setImage(fileName);
        }
        Instant now = Instant.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        productRepository.save(product);
    }

    public PageResponse<ProductResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Product> products = productRepository.findAll(pageRequest);

        return new PageResponse<>(
                ProductConvertor.from(products.getContent(), categoryRepository.findAll()),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isLast()
        );
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product get(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("product not found with %s id", id)));
    }

    @Transactional
    public void update(int id, Product product, MultipartFile file) {
        commonSchemaValidator.categoryExist(product.getCategoryId());
        Product product1 = get(id);

        if(!file.isEmpty()){
            deleteFileByProduct(product1);
            String fileName = FileUtil.uploadImage(file);
            product1.setImage(fileName);
        }

        product1.setName(product.getName());
        product1.setCategoryId(product.getCategoryId());
        product1.setUpdatedAt(Instant.now());
        productRepository.save(product1);
    }

    @Transactional
    public void delete(int id) {
        Product product = get(id);
        deleteFileByProduct(product);
        productRepository.delete(product);
    }

    @Transactional
    public void deleteFileByProduct(Product product) {
        String imagePath = FilePathConstant.FILE_UPLOAD_PATH + product.getImage();
        Path filePath = Paths.get(imagePath);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new FileDeletionException("Could not delete");
        }
    }

}