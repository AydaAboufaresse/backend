package com.ocp.GestionMission.model;

import java.util.Date;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Collaborateur")
@Getter
@Setter

public class Collaborateur extends Utilisateur {

    
}




