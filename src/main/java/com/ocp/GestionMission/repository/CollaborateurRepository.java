package com.ocp.GestionMission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.GestionMission.model.Collaborateur;
import com.ocp.GestionMission.model.Vehicule;

@Repository
public interface CollaborateurRepository extends JpaRepository<Collaborateur, Long> {
	public Optional<Collaborateur> findById(Long id);
}




