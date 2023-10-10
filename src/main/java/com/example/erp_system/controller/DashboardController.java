package com.example.erp_system.controller;

import com.example.erp_system.dto.debt.DebtResponseDto;
import com.example.erp_system.dto.incoming_and_outgoing_to_account.IncomingAndOutgoingToAccountResponseDto;
import com.example.erp_system.dto.pageResponse.PageResponse;
import com.example.erp_system.dto.sales.SalesResponseDto;
import com.example.erp_system.model.Account;
import com.example.erp_system.service.AccountService;
import com.example.erp_system.service.DebtService;
import com.example.erp_system.service.IncomingAndOutgoingToAccountService;
import com.example.erp_system.service.SalesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {
    private final AccountService accountService;
    private final IncomingAndOutgoingToAccountService incomingAndOutgoingToAccountService;
    private final DebtService debtService;
    private final SalesService salesService;

    @GetMapping()
    public String get(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<IncomingAndOutgoingToAccountResponseDto> incomingAndOutgoingToAccounts = incomingAndOutgoingToAccountService.getAll(pageNum, pageSize);
        Account account = accountService.get();
        model.addAttribute("account", account);
        model.addAttribute("incomingAndOutgoingToAccount", incomingAndOutgoingToAccounts);
        return "views/admin/index";
    }

    @GetMapping("/debt")
    public String getDebt(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize

            ) {
        PageResponse<DebtResponseDto> debts = debtService.getAll(pageNum, pageSize);
        Account account = accountService.get();
        model.addAttribute("debts", debts);
        model.addAttribute("account", account);
        return "views/admin/debt";
    }

    @GetMapping("/sales")
    public String getSales(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize

    ) {
        PageResponse<SalesResponseDto> salesResponseDtoPageResponse = salesService.getAll(pageNum, pageSize);
        Account account = accountService.get();
        model.addAttribute("sales", salesResponseDtoPageResponse);
        model.addAttribute("account", account);
        return "views/admin/sales";
    }


}
