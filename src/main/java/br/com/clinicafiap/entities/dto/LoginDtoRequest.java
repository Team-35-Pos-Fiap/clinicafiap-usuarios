package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDtoRequest(
    @NotBlank
    @Email
    String email,
    @NotBlank
    String senha) {
}
