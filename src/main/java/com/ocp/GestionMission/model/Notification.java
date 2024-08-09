package com.ocp.GestionMission.model;
import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinataire;
    private String message;
    private Date dateEnvoi;
    private String statut;

    // Getters and Setters
}


