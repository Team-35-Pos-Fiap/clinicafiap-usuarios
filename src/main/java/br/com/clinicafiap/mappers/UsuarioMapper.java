package br.com.clinicafiap.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.clinicafiap.entities.db.UsuarioDb;
import br.com.clinicafiap.entities.domain.Usuario;
import br.com.clinicafiap.entities.dto.UsuarioDtoRequest;
import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;

@Component
public abstract class UsuarioMapper {

	// record -> domain -> entity
	
	// 1 - record -> domain

	public static Usuario toUsuario(UsuarioDtoRequest usuario) {
		return new Usuario(null,
					       usuario.nome(), 
					       usuario.email(), 
					       usuario.senha(),
					       true,
					       PerfilMapper.toPerfil(usuario.perfil()));
	}

	// 2 - domain -> entity
	
	public static UsuarioDb toUsuarioDb(Usuario usuario) {	
		return new UsuarioDb(usuario.getId(),
							 usuario.getNome(), 
							 usuario.getEmail(), 
							 usuario.getSenha(),
							 usuario.getAtivo(),
							 PerfilMapper.toPerfil(usuario.getPerfil()));
	}
	
	// entity -> domain -> record
	
	// 3 - entity -> domain
	
	public static Usuario toUsuario(UsuarioDb usuario) {
		return new Usuario(usuario.getId(),
						   usuario.getNome(), 
						   usuario.getEmail(), 
						   usuario.getSenha(),
						   usuario.getAtivo(),
						   PerfilMapper.toPerfil(usuario.getPerfil()));
	}
	
	// 4 - domain -> record
	
	public static UsuarioDtoResponse toUsuarioDtoResponse(Usuario usuario) {
		return new UsuarioDtoResponse(usuario.getId(),
									  usuario.getNome(), 
									  usuario.getEmail(), 
									  usuario.getSenha(),
									  usuario.getAtivo(),
									  PerfilMapper.toPerfilDto(usuario.getPerfil()));
	}

	public static List<UsuarioDtoResponse> toListUsuarioDtoResponse(List<UsuarioDb> usuarios) {
		return usuarios.stream().map(u -> UsuarioMapper.toUsuario(u))
								.map(u -> UsuarioMapper.toUsuarioDtoResponse(u))
								.collect(Collectors.toList());
	}
}