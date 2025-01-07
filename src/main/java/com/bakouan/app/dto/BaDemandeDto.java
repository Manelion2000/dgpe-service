package com.bakouan.app.dto;
import com.bakouan.app.enums.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
public class BaDemandeDto {
    /**
     * Information propre à la demande
     */

    private String id ;

    private String numeroDemande;

    private LocalDate dateDemande;

    private LocalDate dateValidation;

    private LocalDate dateValidationDg;

    private LocalDate dateProduction;

    private LocalDate dateRetrait;

    private String motifRejet;

    private EStatus status=EStatus.ENCOURS;

    /**
     * Information propre au demandeur
     */

    private String nom;

    private String prenom;

    private LocalDate dateNaissance;

    private String lieuNaissance;

    private String email;

    private String adresse;

    private String telephone;

    private String profession;

    //private String fonction;

    private String institution;

    private String nomPrenom;

    private String telephoneAprevenir;

    private ESexe sexe;

    private ETypeDemandeur demandeur;

    //private ETypeDemandeurAcces typeDemandeurAcces;

    private ETypeCarte typeCarte;

    private ETypeCarteAcces typeCarteAcces;

    private ECarte eCarte;

    private String particulier;

    private String idUser;
    /**
     * Information concernant la mission diplomatique
     */

    private String idMissionDiplomatique;

    private String libelleMissionDiplomatique;

    private String nationalite;

    private String pays;
    /**
     * Information sur les documents de la demande
     */

    private Set<BaDocumentDto> documents;
}
