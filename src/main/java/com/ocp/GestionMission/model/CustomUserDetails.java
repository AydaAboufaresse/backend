package com.ocp.GestionMission.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private Long id;
    private String nom;
    private String prenom;
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
    private String imageUrl;

    public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, Long id, String nom,
                             String prenom, Date date_naissance, String nomUtilisateur, String motDePasse,
                             String service, String tel, String cin, String position, String email,
                             String genre, String adress, String imageUrl) {
        this.authorities = authorities;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.service = service;
        this.tel = tel;
        this.cin = cin;
        this.position = position;
        this.email = email;
        this.genre = genre;
        this.adress = adress;
        this.imageUrl = imageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.motDePasse;
    }

    @Override
    public String getUsername() {
        return this.nomUtilisateur;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails user = (CustomUserDetails) o;
        return Objects.equals(id, user.id);
    }
}
