package br.com.clinicafiap.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.clinicafiap.entities.db.PerfilDb;
import br.com.clinicafiap.entities.domain.Perfil;
import br.com.clinicafiap.entities.dto.PerfilDtoResponse;

@Component
public abstract class PerfilMapper {
	
	// record -> domain -> entity
	
	// 1 - record -> domain
	public static Perfil toPerfil(Integer idPerfilAcesso) {
		return new Perfil(idPerfilAcesso, null);
	}

	// 2 - domain -> entity
	
	public static PerfilDb toPerfil(Perfil perfil) {
		return new PerfilDb(perfil.getId(), perfil.getNome());
	}
	
	// entity -> domain -> record
	
	// 3 - entity -> domain
	
	public static Perfil toPerfil(PerfilDb perfil) {
		return new Perfil(perfil.getId(), perfil.getNome());
	}
	
	// 4 - domain -> record
	
	public static PerfilDtoResponse toPerfilDto(Perfil perfil) {
		return new PerfilDtoResponse(perfil.getId(), perfil.getNome());
	}

	public static List<PerfilDtoResponse> toPerfilRecord(List<PerfilDb> dados) {
		List<PerfilDtoResponse> perfis = dados.stream()
												.map(PerfilMapper::toPerfil)
												.map(PerfilMapper::toPerfilDto)
												.toList();

		return perfis;
	}
}