package br.com.clinicafiap.repositories.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.clinicafiap.entities.db.UsuarioDb;

public interface IUsuarioRepository {
    UsuarioDb recuperaDadosUsuarioPorId(UUID id);
    List<UsuarioDb> buscarUsuariosPorPerfil(Integer idPerfil);
    void salvar(UsuarioDb usuario);
    boolean emailJaCadastrado(String email);
}