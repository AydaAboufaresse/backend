package com.ocp.GestionMission.service;

import com.ocp.GestionMission.model.Mission;

import java.util.List;
import java.util.Optional;

public interface MissionService {
    List<Mission> getAllMissions();
    Mission getMissionById(Long id);
    Mission createMission(Mission mission);
    Mission updateMission(Long id, Mission missionDetails);
    boolean deleteMission(Long id);
}
