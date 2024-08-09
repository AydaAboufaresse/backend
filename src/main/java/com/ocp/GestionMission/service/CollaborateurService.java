package com.ocp.GestionMission.service;

import com.ocp.GestionMission.model.Collaborateur;

import java.util.List;
import java.util.Optional;

public interface CollaborateurService {
    List<Collaborateur> findAll();
    Optional<Collaborateur> findById(Long id);
    Collaborateur save(Collaborateur collaborateur);
    void deleteById(Long id);
    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean changePassword(Long id, String oldPassword, String newPassword);
    boolean updateLocation(Long id, Double latitude, Double longitude);
}
