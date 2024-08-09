package com.ocp.GestionMission.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.GestionMission.model.Vehicule;



@Repository
public interface VehiculesRepository extends JpaRepository<Vehicule, String> {
 
	 public Optional<Vehicule> findById(String id);
	 List<Vehicule> findByMarqueContainingOrModeleContainingOrImmatriculationContainingOrServiceContaining(String marque, String modele, String immatriculation, String service);

}