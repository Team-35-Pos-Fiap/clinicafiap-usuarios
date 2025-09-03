package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PerfilDtoRequest(@NotNull(message = "É necessário informar o perfil de acesso para o usuário.")
							   @Positive(message = "O id do perfil precisa ser maior do que 0.")
							   Integer idPerfil) { }
