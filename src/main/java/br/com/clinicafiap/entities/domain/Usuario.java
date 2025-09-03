package br.com.clinicafiap.entities.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Usuario {
	private UUID id;
	private String nome;
	private String email;
	private String senha;
	private Boolean ativo;	
	private Perfil perfil;
}