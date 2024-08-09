package com.ocp.GestionMission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.ocp.GestionMission.model.Responsable;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
	public Optional<Responsable> findById(Long id);
	Optional<Responsable> findByNomUtilisateur(String nomUtilisateur);
}



