package com.example.erp_system.controller;

import com.example.erp_system.convert.UserConverter;
import com.example.erp_system.dto.user.UpdateUsernameOfUserRequestDto;
import com.example.erp_system.dto.user.UserCreateRequestDto;
import com.example.erp_system.dto.user.UserResponseDto;
import com.example.erp_system.dto.user.UpdatePasswordOfUserRequestDto;
import com.example.erp_system.model.User;
import com.example.erp_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public String register(
            Model model,
            @ModelAttribute UserCreateRequestDto userCreateRequestDto
    ) {
        User user = UserConverter.convertToDomain(userCreateRequestDto);
        User save = userService.save(user);
        UserResponseDto userResponseDto = UserConverter.from(save);
        model.addAttribute("user", userResponseDto);
        return "views/auth/login";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "views/profile/index";
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasRole('ADMIN')")
    public String welcome() {
        return "layout";
    }

    @PostMapping("/update-password")
    public RedirectView updatePassword(
            @ModelAttribute UpdatePasswordOfUserRequestDto updatePasswordOfUserRequestDto
            ) {
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("/profile");

        if(updatePasswordOfUserRequestDto.getCurrentPassword().equals(updatePasswordOfUserRequestDto.getNewPassword())) {
            redirectView.addStaticAttribute("warning_password", "New password is already in use");
            return redirectView;
        }

        boolean result = userService.updatePassword(updatePasswordOfUserRequestDto);

        if(result) {
            redirectView.addStaticAttribute("success_password", "Password updated successfully");
        } else {
            redirectView.addStaticAttribute("error_password", "Password not updated successfully");
        }
        return redirectView;
    }

    @PostMapping("/update-username")
    public RedirectView updateUsername(
            @ModelAttribute UpdateUsernameOfUserRequestDto updateUsernameOfUserRequestDto
            ) {
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("/profile");

        if(updateUsernameOfUserRequestDto.getNewUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            redirectView.addStaticAttribute("warning_username", "New username is already in use");
            return redirectView;
        }

        boolean result = userService.updateUsername(updateUsernameOfUserRequestDto);

        if(result) {
            redirectView.addStaticAttribute("success_username", "Username updated successfully");
        } else {
            redirectView.addStaticAttribute("error_username", "Username not updated successfully");
        }
        return redirectView;
    }

}
