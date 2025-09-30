package br.com.clinicafiap.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.clinicafiap.entities.db.UsuarioDb;

public interface IUsuarioJpaRepository extends JpaRepository<UsuarioDb, UUID> {
	@Query("SELECT u FROM UsuarioDb u INNER JOIN u.perfil p WHERE p.id = :id and u.ativo = true")
	List<UsuarioDb> buscarUsuariosPorPerfil(@Param("id") Integer idPerfil);
	Optional<UsuarioDb> findByEmail(String email);
	boolean existsByEmail(String email);
}