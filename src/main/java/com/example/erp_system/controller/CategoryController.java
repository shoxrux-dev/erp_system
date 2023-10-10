package com.example.erp_system.controller;

import com.example.erp_system.convert.CategoryConverter;
import com.example.erp_system.dto.category.CategoryCreateRequestDto;
import com.example.erp_system.dto.category.CategoryResponseDto;
import com.example.erp_system.dto.category.CategoryUpdateRequestDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.model.Category;
import com.example.erp_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public String getAll(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<CategoryResponseDto> categories = categoryService.getAll(pageNum, pageSize);
        model.addAttribute("categories", categories);
        return "views/category/index";
    }

    @GetMapping("/add")
    public String add() {
        return "views/category/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute CategoryCreateRequestDto categoryCreateRequestDto
    ) {
        Category category = CategoryConverter.convertToDomain(categoryCreateRequestDto);
        categoryService.create(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        categoryService.delete(id);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Category category = categoryService.get(id);
        model.addAttribute("category", category);
        return "views/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute CategoryUpdateRequestDto categoryUpdateRequestDto
    ) {
        Category category = CategoryConverter.convertToDomain(categoryUpdateRequestDto);
        categoryService.update(id, category);
        return "redirect:/category";
    }
}
