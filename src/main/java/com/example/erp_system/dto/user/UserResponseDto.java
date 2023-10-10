package com.example.erp_system.dto.user;

import com.example.erp_system.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    String username;
    List<Role> roles;
    String image;
}
