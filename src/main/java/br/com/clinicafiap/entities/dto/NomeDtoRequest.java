package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NomeDtoRequest(@NotBlank(message = "O campo nome precisa estar preenchido.")
                                @Size(min = 3, max = 150, message = "O campo nome precisa ter entre 3 e 150 caracteres.")
                                String nome) { }
