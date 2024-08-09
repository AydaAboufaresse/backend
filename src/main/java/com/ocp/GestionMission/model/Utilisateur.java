package com.ocp.GestionMission.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name ="utilisateur")
@Getter
@Setter

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)

public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date_naissance;
    
    private String nomUtilisateur;
    private String motDePasse;
    private String service;
    private String tel;
    private String cin;
    private String position;
    private String email;
    private String genre;
    private String adress;

    private String imageUrl=null;

    // Getters and Setters
    
    public Utilisateur(){
    	
    }
    

  
}
