package br.com.clinicafiap.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.clinicafiap.entities.db.PerfilDb;
import br.com.clinicafiap.entities.dto.PerfilDtoResponse;
import br.com.clinicafiap.mappers.PerfilMapper;
import br.com.clinicafiap.repositories.interfaces.IPerfilRepository;
import br.com.clinicafiap.services.interfaces.IPerfilService;

@Service
public class PerfilService implements IPerfilService {

    private final IPerfilRepository perfilRepository;

    public PerfilService(IPerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    public PerfilDb buscarPorId(Integer id) {
        return perfilRepository.buscarPorId(id);
    }

    @Override
    public List<PerfilDtoResponse> buscarTodos() {
        return PerfilMapper.toPerfilRecord(perfilRepository.buscarTodos());
    }
}