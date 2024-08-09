package com.ocp.GestionMission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.GestionMission.model.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
}



