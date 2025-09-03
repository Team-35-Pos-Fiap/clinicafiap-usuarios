package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.NotBlank;


public record SenhaDtoRequest(@NotBlank(message = "O campo senha precisa estar preenchido.")
                                 String senha) { }
