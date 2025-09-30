package br.com.clinicafiap.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import br.com.clinicafiap.entities.db.UsuarioDb;
import br.com.clinicafiap.repositories.interfaces.IUsuarioJpaRepository;
import br.com.clinicafiap.repositories.interfaces.IUsuarioRepository;
import br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException;
import br.com.clinicafiap.utils.MensagensUtil;

@Repository
public class UsuarioRepository implements IUsuarioRepository {

	private final IUsuarioJpaRepository usuarioRepository;

	public UsuarioRepository(IUsuarioJpaRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Cacheable(value = "usuario", key = "#id")
	public UsuarioDb recuperaDadosUsuarioPorId(UUID id) {
		return getUsuarioEntity(usuarioRepository.findById(id));
	}

	@Override
	public UsuarioDb recuperarDaodsUsuarioPorEmail(String email) {
		return getUsuarioEntity(usuarioRepository.findByEmail(email));
	}

	@Override
	@Cacheable(value = "usuarios_por_perfil", key = "#idPerfil")
	public List<UsuarioDb> buscarUsuariosPorPerfil(Integer idPerfil) {
		List<UsuarioDb> usuarios = usuarioRepository.buscarUsuariosPorPerfil(idPerfil);
		
		if(usuarios.isEmpty()) {		
			throw new UsuarioNaoEncontradoException(MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_USUARIOS_NAO_ENCONTRADOS));
		} else {
			return usuarios;
		}
	}
	
	@Override
	@CacheEvict(value = "usuarios_por_perfil", key = "#usuario.perfil.id")
	public void salvar(UsuarioDb usuario) {
		usuarioRepository.save(usuario);
	}

	@Override
	public boolean emailJaCadastrado(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	private UsuarioDb getUsuarioEntity(Optional<UsuarioDb> dadosUsuario) {
		if(dadosUsuario.isPresent()) {
			return dadosUsuario.get();
		} else {
			throw new UsuarioNaoEncontradoException(MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_USUARIO_NAO_ENCONTRADO));
		}
	}
}