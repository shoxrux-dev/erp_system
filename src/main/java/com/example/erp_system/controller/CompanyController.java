package com.example.erp_system.controller;

import com.example.erp_system.convert.CompanyConverter;
import com.example.erp_system.dto.company.CompanyCreateRequestDto;
import com.example.erp_system.dto.company.CompanyResponseDto;
import com.example.erp_system.dto.company.CompanyUpdateRequestDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.model.Company;
import com.example.erp_system.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping()
    public String get(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<CompanyResponseDto> companies = companyService.getAll(pageNum, pageSize);
        model.addAttribute("companies", companies);
        return "views/company/index";
    }

    @GetMapping("/add")
    public String add() {
        return "views/company/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute CompanyCreateRequestDto companyCreateRequestDto
            ) {
        Company company = CompanyConverter.convertToDomain(companyCreateRequestDto);
        companyService.create(company);
        return "redirect:/company";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        companyService.delete(id);
        return "redirect:/company";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Company company = companyService.get(id);
        model.addAttribute("company", company);
        return "views/company/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute CompanyUpdateRequestDto companyUpdateRequestDto
    ) {
        Company company = CompanyConverter.convertToDomain(companyUpdateRequestDto);
        companyService.update(id, company);
        return "redirect:/company";
    }

}
