package br.com.clinicafiap.entities.db;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Getter
@Setter
public class UsuarioDb {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String nome;
	
	@Column(unique = true)
	private String email;
	
	private String senha;

	private Boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "id_perfil", columnDefinition = "int")
	private PerfilDb perfil;
	
	public void inativar() {
		this.ativo = false;
	}
	
	public void reativar() {
		this.ativo = true;
	}

	/*public void atualizarPerfil(PerfilEntity perfil) {
		this.perfil = perfil;
	}*/
	
	public void atualizarNome(String nome) {
		this.nome = nome;
	}
	
	public void atualizarEmail(String email) {
		this.email = email;
	}
	
	public void atualizarSenha(String senha) {
		this.senha = senha;
	}
}