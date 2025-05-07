package com.bakouan.app.dto;

import com.bakouan.app.enums.ECarte;
import com.bakouan.app.enums.ESexe;
import com.bakouan.app.enums.ETypeDemandeur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaCarteDto {
    private String id;

    private LocalDate dateProduction;

    private LocalDate dateExpiration;

    private String codeProduction;

    private String codeCarte;

    private String codeBarre;
    /**
     * Information sur la demande
     */
    private String idDemande;

    private String numeroDemande;

    private LocalDate dateDemande;

    private String nom;

    private String prenom;

    private LocalDate dateNaissance;

    private String lieuNaissance;

    private String institution;

    private String fonction;

    private ESexe sexe;

    private ECarte carte;

    private ETypeDemandeur demandeur;



    Integer nombreDeMois;

}
