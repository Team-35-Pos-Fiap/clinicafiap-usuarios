package br.com.clinicafiap.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UsuarioDtoRequest(@NotBlank(message = "O campo nome precisa estar preenchido.")
							    @Size(min = 3, max = 150, message = "O campo nome precisa ter entre 3 e 150 caracteres.") 
							    String nome, 

							    @NotBlank(message = "O campo email precisa estar preenchido.")
							    @Email(message = "O e-mail precisa ser válido")
						        @Size(min = 3, max = 50, message = "O campo email precisa ter entre 3 e 70 caracteres.") 
							    String email, 
							 	   
							    @NotBlank(message = "O campo senha precisa estar preenchido.")
							    @Size(min = 3, max = 10, message = "O campo email precisa ter entre 3 e 70 caracteres.") 
							    String senha, 
							   
							    @NotNull(message = "É necessário informar o perfil de acesso para o usuário.")
							    @Positive(message = "O id do perfil precisa ser maior do que 0.")
							    Integer perfil) { }