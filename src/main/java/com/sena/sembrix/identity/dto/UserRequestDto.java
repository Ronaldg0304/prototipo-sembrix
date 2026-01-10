package com.sena.sembrix.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;

    private String email;

    private String password;

    private String role;
    private String status;
}
