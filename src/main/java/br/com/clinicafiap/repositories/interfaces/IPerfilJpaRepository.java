package br.com.clinicafiap.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.clinicafiap.entities.db.PerfilDb;

public interface IPerfilJpaRepository extends JpaRepository<PerfilDb, Integer> { }
