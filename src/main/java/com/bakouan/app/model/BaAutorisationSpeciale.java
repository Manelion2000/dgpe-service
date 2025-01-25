package com.bakouan.app.model;

import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ba_autorisation_speciale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaAutorisationSpeciale extends BaAbstractAuditingEntity {
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "nom_demandeur")
    @NotBlank(message = "Le nom du demandeur est obligatoire")
    private String nomDemandeur;

    @Column(name = "fonction_demandeur")
    @NotBlank(message = "La fonction du demandeur est obligatoire")
    private String fonctionDemandeur;

    @Column(name = "date_arrivee")
    @NotNull(message = "La date d'arrivée est obligatoire")
    private LocalDate dateArrivee;

    @Column(name = "date_depart")
    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    @Column(name = "etat")
    @Enumerated(EnumType.STRING)
    private EEtatAutorisation etat = EEtatAutorisation.EN_ATTENTE;

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaDocumentAutorisatinSpecial> documents = new ArrayList<>();

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaDelegationMembre> delegation = new ArrayList<>();
}
