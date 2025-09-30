package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDtoRequest(
    @NotBlank(message = "O campo email precisa estar preenchido.")
    @Email(message = "O campo email precisa ser valido.")
    String email,
    @NotBlank(message = "O campo senha precisa estar preenchido.")
    String senha) {
}
