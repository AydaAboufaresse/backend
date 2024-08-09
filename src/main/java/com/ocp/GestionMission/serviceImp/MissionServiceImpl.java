package com.ocp.GestionMission.serviceImp;

import com.ocp.GestionMission.model.Collaborateur;
import com.ocp.GestionMission.model.Mission;
import com.ocp.GestionMission.model.Utilisateur;
import com.ocp.GestionMission.model.Vehicule;
import com.ocp.GestionMission.repository.MissionRepository;
import com.ocp.GestionMission.repository.UtilisateurRepository;
import com.ocp.GestionMission.repository.VehiculesRepository;
import com.ocp.GestionMission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private VehiculesRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;




    @Override
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Override
    public Mission getMissionById(Long id) {
        Optional<Mission> optionalMission = missionRepository.findById(id);
        return optionalMission.orElse(null);  // Return null if mission is not found
    }

    @Override
    public Mission createMission(Mission mission) {
        if (mission.getCollaborateur().getId() == null) {
            throw new IllegalArgumentException("L'ID du collaborateur ne doit pas être nul.");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(mission.getCollaborateur().getId());
        if (utilisateurOpt.isEmpty() || !(utilisateurOpt.get() instanceof Collaborateur)) {
            throw new IllegalArgumentException("Le collaborateur spécifié n'existe pas ou n'est pas un collaborateur.");
        }


        if (mission.getVehicule().getVehiculeId() == null) {
            throw new IllegalArgumentException("L'ID du véhicule ne doit pas être nul.");
        }

        Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(mission.getVehicule().getVehiculeId());
        if (vehiculeOpt.isEmpty()) {
            throw new IllegalArgumentException("Le véhicule spécifié n'existe pas.");
        }

        mission.setVehicule(vehiculeOpt.get());

        return missionRepository.save(mission);
    }


    @Override
    public Mission updateMission(Long id, Mission missionDetails) {
        Mission existingMission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id " + id));

        if (missionDetails.getCollaborateur().getId() == null) {
            throw new IllegalArgumentException("L'ID du collaborateur ne doit pas être nul.");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(missionDetails.getCollaborateur().getId());
        if (utilisateurOpt.isEmpty() || !(utilisateurOpt.get() instanceof Collaborateur)) {
            throw new IllegalArgumentException("Le collaborateur spécifié n'existe pas ou n'est pas un collaborateur.");
        }


        if (missionDetails.getVehicule().getVehiculeId() == null) {
            throw new IllegalArgumentException("L'ID du véhicule ne doit pas être nul.");
        }

        Vehicule vehicule = vehiculeRepository.findById(missionDetails.getVehicule().getVehiculeId())
                .orElseThrow(() -> new IllegalArgumentException("Le véhicule spécifié n'existe pas."));

        existingMission.setTitre(missionDetails.getTitre());
        existingMission.setDescription(missionDetails.getDescription());
        existingMission.setMission_longitude(missionDetails.getMission_longitude());
        existingMission.setMission_latitude(missionDetails.getMission_latitude());
        existingMission.setDateDebut(missionDetails.getDateDebut());
        existingMission.setDateFin(missionDetails.getDateFin());
        existingMission.setStatut(missionDetails.getStatut());
        existingMission.setCollaborateur(missionDetails.getCollaborateur());
        existingMission.setVehicule(vehicule);

        return missionRepository.save(existingMission);
    }
    @Override
    public boolean deleteMission(Long id) {
        Optional<Mission> optionalMission = missionRepository.findById(id);
        if (optionalMission.isPresent()) {
            Mission mission = optionalMission.get();
            Vehicule vehicule = mission.getVehicule();

            vehicule.setStatus(0);
            vehiculeRepository.save(vehicule);

            missionRepository.deleteById(id);
            return true;
        } else {
            return false;  // Or throw an exception if the mission is not found
        }
    }

}
