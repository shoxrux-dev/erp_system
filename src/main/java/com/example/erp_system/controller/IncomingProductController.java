package com.example.erp_system.controller;

import com.example.erp_system.convert.IncomingProductConverter;
import com.example.erp_system.dto.incoming_product.IncomingProductCreateRequestDto;
import com.example.erp_system.dto.incoming_product.IncomingProductResponseDto;
import com.example.erp_system.dto.incoming_product.IncomingProductUpdateRequestDto;
import com.example.erp_system.dto.incoming_product_to_inventory.IncomingProductToInventoryResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.model.*;
import com.example.erp_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/incoming-product")
public class IncomingProductController {
    private final IncomingProductService incomingProductService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final InventoryService inventoryService;
    private final IncomingProductToInventoryService incomingProductToInventoryService;

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<IncomingProductResponseDto> incomingProducts = incomingProductService.getAll(pageNum, pageSize);
        model.addAttribute("incomingProduct", incomingProducts);
        return "views/incoming_product/index";
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
        return "views/incoming_product/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute IncomingProductCreateRequestDto incomingProductCreateRequestDto
            ) {
        IncomingProduct incomingProduct = IncomingProductConverter.convertToDomain(incomingProductCreateRequestDto);
        incomingProductService.create(incomingProduct);
        return "redirect:/incoming-product";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        IncomingProduct incomingProduct = incomingProductService.get(id);
        Product product = productService.get(incomingProduct.getProductId());
        Company company = companyService.get(incomingProduct.getCompanyId());
        Inventory inventory = inventoryService.get(incomingProduct.getInventoryId());
        IncomingProductResponseDto incomingProductResponseDto = IncomingProductConverter.from(incomingProduct, product, company, inventory);
        List<Product> products = productService.getAll();
        List<Company> companies = companyService.getAll();
        List<Inventory> inventories = inventoryService.getAll();

        model.addAttribute("incomingProduct", incomingProductResponseDto);
        model.addAttribute("products", products);
        model.addAttribute("companies", companies);
        model.addAttribute("inventories", inventories);
        return "views/incoming_product/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute IncomingProductUpdateRequestDto incomingProductUpdateRequestDto
    ) {
        IncomingProduct incomingProduct = IncomingProductConverter.convertToDomain(incomingProductUpdateRequestDto);
        incomingProductService.update(id, incomingProduct);
        return "redirect:/incoming-product";
    }

    @GetMapping("/to-inventory")
    public String getIncomingProductToInventory(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<IncomingProductToInventoryResponseDto> incomingProductToInventories= incomingProductToInventoryService.getAll(pageNum, pageSize);
        model.addAttribute("incomingProductToInventories", incomingProductToInventories);
            return "views/incoming_product/incoming_product_to_inventory";
    }

}
