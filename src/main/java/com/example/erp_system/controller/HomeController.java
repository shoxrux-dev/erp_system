package com.example.erp_system.controller;

import com.example.erp_system.dto.user.UserResponseDto;
import com.example.erp_system.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    ObjectMapper objectMapper;

    @GetMapping
    public String get(@AuthenticationPrincipal User user, Model model) {
        if(Objects.nonNull(user)) {
            UserResponseDto userResponseDto = objectMapper.convertValue(user, UserResponseDto.class);
            model.addAttribute("user", userResponseDto);
        }
        return "views/index";
    }

}
