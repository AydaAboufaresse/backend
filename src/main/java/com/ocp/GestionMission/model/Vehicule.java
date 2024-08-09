package com.ocp.GestionMission.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "vehicule_id")
	private String vehiculeId;
	
	@Column(name = "marque")
	private String marque;
	
	@Column(name = "modele")
	private String modele;
	
	@Column(name = "immatriculation")
	private String immatriculation;
	
	@Column(name = "status")
	private Integer status=0;
	
	@Column(name = "service")
	private String service;

	@Column(name = "vehicule_image_url")
	private String vehiculeImageUrl;

	public String getVehiculeId() {
		return vehiculeId;
	}

	public void setVehiculeId(String vehiculeId) {
		this.vehiculeId = vehiculeId;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getModele() {
		return modele;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}

	public String getImmatriculation() {
		return immatriculation;
	}

	public void setImmatriculation(String immatriculation) {
		this.immatriculation = immatriculation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVehiculeImageUrl() {
		return vehiculeImageUrl;
	}

	public void setVehiculeImageUrl(String vehiculeImageUrl) {
		this.vehiculeImageUrl = vehiculeImageUrl;
	}
}
