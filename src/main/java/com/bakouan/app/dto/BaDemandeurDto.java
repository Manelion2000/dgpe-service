package com.bakouan.app.dto;

import com.bakouan.app.enums.ESexe;
import com.bakouan.app.enums.ETypeDemandeur;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class BaDemandeurDto {

    private String id;

    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "le prénom est obligatoire")
    private String prenom;

    private LocalDate dateNaissance;

    private String lieuNaissance;

    @NotBlank(message = "l'email est obligatoire")
    private String email;

    private String adresse;

    @NotBlank(message = "le télephone est obligaoire")
    private String telephone;

    private String nationalite;


    private String profession;

    @NotBlank(message = "le nom et prenom de la personne à prévenir est obligatoire")
    private String nomPrenom;

    @NotBlank(message = "le télephone de la personne à prévenir est obligatoire")
    private String telephoneAprevenir;


    /**
     * Le genre de l'utilisateur.
     */
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "le sexe est obligatoire")
    private ESexe sexe;

    /**
     * Le type de demandeur.
     */
    @NotNull(message = "le type de demandeur est obligatoire")
    private ETypeDemandeur typeDemandeur;
}
