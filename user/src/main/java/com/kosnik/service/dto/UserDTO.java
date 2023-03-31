package com.kosnik.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosnik.domain.Role;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Email
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   // @Pattern(regexp = "/(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}/g")
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;
}
