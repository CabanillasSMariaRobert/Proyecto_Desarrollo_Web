package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetearClaveRequest {

    @NotBlank @Size(max = 64) private String token;
    @NotBlank @Size(min = 6, max = 100) private String claveNueva;
}