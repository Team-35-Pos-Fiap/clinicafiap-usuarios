package br.com.clinicafiap.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.clinicafiap.entities.db.PerfilDb;
import br.com.clinicafiap.repositories.interfaces.IPerfilJpaRepository;
import br.com.clinicafiap.repositories.interfaces.IPerfilRepository;
import br.com.clinicafiap.services.exceptions.PerfilNaoEncontradoException;
import br.com.clinicafiap.utils.MensagensUtil;

@Repository
public class PerfilRepository implements IPerfilRepository {

    private final IPerfilJpaRepository perfilRepository;

	public PerfilRepository(IPerfilJpaRepository perfilRepository) {
		this.perfilRepository = perfilRepository;
	}

	@Override
	public PerfilDb buscarPorId(Integer id) {
		return getPerfil(perfilRepository.findById(id));
	}

	@Override
	public List<PerfilDb> buscarTodos() {
		return perfilRepository.findAll();
	}

	private PerfilDb getPerfil(Optional<PerfilDb> dados) {
		if(dados.isPresent()) {
			return dados.get();
		} else {
			throw new PerfilNaoEncontradoException(MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PERFIL_NAO_ENCONTRADO));
		}
	}
}
