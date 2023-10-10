package com.example.erp_system.controller;

import com.example.erp_system.convert.InventoryConverter;
import com.example.erp_system.dto.inventory.InventoryCreateRequestDto;
import com.example.erp_system.dto.inventory.InventoryResponseDto;
import com.example.erp_system.dto.inventory.InventoryUpdateRequestDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.model.Inventory;
import com.example.erp_system.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping()
    public String getAll(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<InventoryResponseDto> inventories = inventoryService.getAll(pageNum, pageSize);
        model.addAttribute("inventories", inventories);
        return "views/inventory/index";
    }

    @GetMapping("/add")
    public String add() {
        return "views/inventory/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute InventoryCreateRequestDto inventoryCreateRequestDto
    ) {
        Inventory inventory = InventoryConverter.convertToDomain(inventoryCreateRequestDto);
        inventoryService.create(inventory);
        return "redirect:/inventory";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        inventoryService.delete(id);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Inventory inventory = inventoryService.get(id);
        model.addAttribute("inventory", inventory);
        return "views/inventory/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute InventoryUpdateRequestDto inventoryUpdateRequestDto
    ) {
        Inventory inventory = InventoryConverter.convertToDomain(inventoryUpdateRequestDto);
        inventoryService.update(id, inventory);
        return "redirect:/inventory";
    }

}

