package com.example.erp_system.controller;

import com.example.erp_system.convert.OutgoingProductConverter;
import com.example.erp_system.dto.outgoing_product.OutgoingProductCreateRequestDto;
import com.example.erp_system.dto.outgoing_product.OutgoingProductResponseDto;
import com.example.erp_system.dto.outgoing_product.OutgoingProductUpdateRequestDto;
import com.example.erp_system.dto.outgoing_product_from_inventory.OutgoingProductFromInventoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.model.*;
import com.example.erp_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/outgoing-product")
@RequiredArgsConstructor
public class OutgoingProductController {
    private final OutgoingProductService outgoingProductService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final InventoryService inventoryService;
    private final OutgoingProductFromInventoryService outgoingProductFromInventoryService;

    @GetMapping
    public String get(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<OutgoingProductResponseDto> outgoingProductResponseDto = outgoingProductService.getAll(pageNum, pageSize);
        model.addAttribute("outgoingProducts", outgoingProductResponseDto);
        return "views/outgoing_product/index";
    }

    @GetMapping("/add")
    public String add(Model model)
    {
        List<Product> products = productService.getAll();
        List<Company> companies = companyService.getAll();
        List<Inventory> inventories = inventoryService.getAll();

        model.addAttribute("products", products);
        model.addAttribute("companies", companies);
        model.addAttribute("inventories", inventories);
        return "views/outgoing_product/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute OutgoingProductCreateRequestDto outgoingProductCreateRequestDto
    ) {
        OutgoingProduct outgoingProduct = OutgoingProductConverter.convertToDomain(outgoingProductCreateRequestDto);
        outgoingProductService.create(outgoingProduct);
        return "redirect:/outgoing-product";
    }

    @GetMapping("/from-inventory")
    public String getIncomingProductToInventory(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<OutgoingProductFromInventoryResponseDto> outgoingProductFromInventories= outgoingProductFromInventoryService.getAll(pageNum, pageSize);
        model.addAttribute("outgoingProductFromInventories", outgoingProductFromInventories);
        return "views/outgoing_product/outgoing_product_from_inventory";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        OutgoingProduct outgoingProduct = outgoingProductService.get(id);
        Product product = productService.get(outgoingProduct.getProductId());
        Company company = companyService.get(outgoingProduct.getCompanyId());
        Inventory inventory = inventoryService.get(outgoingProduct.getInventoryId());
        OutgoingProductResponseDto outgoingProductResponseDto = OutgoingProductConverter.from(outgoingProduct, product, company, inventory);
        List<Product> products = productService.getAll();
        List<Company> companies = companyService.getAll();
        List<Inventory> inventories = inventoryService.getAll();

        model.addAttribute("outgoingProduct", outgoingProductResponseDto);
        model.addAttribute("products", products);
        model.addAttribute("companies", companies);
        model.addAttribute("inventories", inventories);
        return "views/outgoing_product/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute OutgoingProductUpdateRequestDto outgoingProductUpdateRequestDto
    ) {
        OutgoingProduct outgoingProduct = OutgoingProductConverter.convertToDomain(outgoingProductUpdateRequestDto);
        outgoingProductService.update(id, outgoingProduct);
        return "redirect:/outgoing_product";
    }

}
