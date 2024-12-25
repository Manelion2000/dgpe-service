package com.bakouan.app.model;

import com.bakouan.app.enums.*;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="ba_demande")
public class BaDemande extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "numero_demande",unique = true)
    private String numeroDemande;
    /**
     * Information sur le demandeur
     */
    @Column(name = "nom", length = 50)
    private String nom;

    @NotNull
    @Column(name = "prenom", length = 100)
    private String prenom;


    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "email",  unique = true,length = 100)
    private String email;

    @Column(name = "adresse", length = 100)
    private String adresse;

    @Column(name = "telephone",  unique = true)
    private String telephone;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "profession")
    private String profession;

    @Column(name = "date_prise_fonction")
    private LocalDate datePriseFonction;

    @Column(name = "institution")
    private String institution;

    @Column(name = "personne_aprevenir", length = 100)
    private String nomPrenom;

    @Column(name = "telephone_aprevenir")
    private String telephoneAprevenir;


    /**
     * Le genre de l'utilisateur.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "sexe")
    private ESexe sexe;

    /**
     * Le type de demandeur.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_demandeur")
    private ETypeDemandeur typeDemandeur;

    /**
     * Le type de demandeur.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_demandeur_acces")
    private ETypeDemandeurAcces typeDemandeurAcces;

    /**
     * Le type de carte diplomatique.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_carte")
    private ETypeCarte typeCarte;

    /**
     * Le type de carte diplomatique.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "ecarte")
    private ECarte eCarte;

    /**
     * Le type de carte accès au salons officiels.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_carte_acces")
    private ETypeCarteAcces typeCarteAcces;



    @Column(name = "particulier")
    private String particulier;

    /**
     * Information sur la demande
     */

    @Column(name = "date_demande")
    private LocalDate dateDemande;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(name="date_validation_dg")
    private LocalDate dateValidationDg;

    @Column(name="date_production")
    private LocalDate dateProduction;

    @Column(name="date_retrait")
    private LocalDate dateRetrait;

    @Column(name = "motif")
    private String motifRejet;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private EStatus status=EStatus.ENCOURS;

    @OneToMany(mappedBy = "demande", fetch = FetchType.LAZY)
    private Set<BaDocument> documents = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaUser user;

    @ManyToOne
    @JoinColumn(name="mission_diplomatique_id")
    private BaMissionDiplomatique missionDiplomatique;

}
