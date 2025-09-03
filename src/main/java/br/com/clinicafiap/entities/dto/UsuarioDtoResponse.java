package br.com.clinicafiap.entities.dto;

import java.util.UUID;

public record UsuarioDtoResponse(UUID id,
							     String nome,
								 String email,
								 String senha,
								 Boolean ativo,
								 PerfilDtoResponse perfil) { }