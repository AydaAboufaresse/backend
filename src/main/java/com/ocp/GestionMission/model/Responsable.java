package com.ocp.GestionMission.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.Getter;

import lombok.Setter;

@Entity
@DiscriminatorValue("Responsable")
@Getter
@Setter
public class Responsable extends Utilisateur {
    
}





