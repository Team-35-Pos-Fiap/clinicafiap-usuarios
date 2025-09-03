package br.com.clinicafiap.services.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.clinicafiap.entities.domain.Usuario;
import br.com.clinicafiap.entities.dto.UsuarioDtoRequest;
import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface IUsuarioService {
    UsuarioDtoResponse buscarPorId(UUID id);
    List<UsuarioDtoResponse> buscarUsuariosPorPerfil(Integer idPerfil);
    void cadastrar(UsuarioDtoRequest usuario);
    void atualizarStatus(UUID id, boolean isAtivo);
    void atualizarNome(UUID id, String nome);
    void atualizarEmail(UUID id, String email);
    /*
    void atualizarPerfil(UUID id, Integer idPerfil);*/
	void atualizarSenha(UUID id, String senha);
}
