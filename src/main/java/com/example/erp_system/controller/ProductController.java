package com.example.erp_system.controller;

import com.example.erp_system.convert.ProductConvertor;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.dto.product.ProductCreateRequestDto;
import com.example.erp_system.dto.product.ProductResponseDto;
import com.example.erp_system.dto.product.ProductUpdateRequestDto;
import com.example.erp_system.dto.product_in_inventory.ProductInInventoryResponseDto;
import com.example.erp_system.model.Category;
import com.example.erp_system.model.Product;
import com.example.erp_system.service.CategoryService;
import com.example.erp_system.service.ProductInInventoryService;
import com.example.erp_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductInInventoryService productInInventoryService;

    @GetMapping()
    public String getAll(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<ProductResponseDto> products = productService.getAll(pageNum, pageSize);
        model.addAttribute("products", products);
        return "views/product/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "views/product/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute ProductCreateRequestDto productCreateRequestDto
    ) {
        Product product = ProductConvertor.convertToDomain(productCreateRequestDto);
        productService.create(product, productCreateRequestDto.getImage());
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productService.get(id);
        ProductResponseDto productResponseDto = ProductConvertor.from(product, categoryService.get(product.getCategoryId()));
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", productResponseDto);
        return "views/product/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute ProductUpdateRequestDto productUpdateRequestDto
    ) {
        Product product = ProductConvertor.convertToDomain(productUpdateRequestDto);
        productService.update(id, product, productUpdateRequestDto.getImage());
        return "redirect:/product";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/product";
    }

    @GetMapping("/in-inventory")
    public String getProductInInventory(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<ProductInInventoryResponseDto> productInInventories = productInInventoryService.getAll(pageNum, pageSize);
        model.addAttribute("productInInventories", productInInventories);
        return "views/product/product_in_inventory";
    }

}
