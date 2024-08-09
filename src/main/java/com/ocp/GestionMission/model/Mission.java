package com.ocp.GestionMission.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "mission")
@Getter
@Setter
public class Mission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private double mission_longitude;
    private double mission_latitude;
    private Date dateDebut;
    private Date dateFin;
    private String statut;

    @ManyToOne
    @JoinColumn(name = "collaborateur_id", referencedColumnName = "id")
    private Collaborateur collaborateur;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", referencedColumnName = "vehicule_id")
    private Vehicule vehicule;
}
