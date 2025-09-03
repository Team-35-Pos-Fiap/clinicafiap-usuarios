package br.com.clinicafiap.repositories.interfaces;

import java.util.List;

import br.com.clinicafiap.entities.db.PerfilDb;

public interface IPerfilRepository {
    PerfilDb buscarPorId(Integer id);
    List<PerfilDb> buscarTodos();
}
