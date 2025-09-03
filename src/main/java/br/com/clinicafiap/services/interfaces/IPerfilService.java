package br.com.clinicafiap.services.interfaces;

import java.util.List;

import br.com.clinicafiap.entities.db.PerfilDb;
import br.com.clinicafiap.entities.dto.PerfilDtoResponse;

public interface IPerfilService {

    PerfilDb buscarPorId(Integer id);
    List<PerfilDtoResponse> buscarTodos();
}
