package com.bakouan.app.model;

import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "date_arrivee")

    private LocalDate dateArrivee;

    @Column(name = "date_depart")
    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    @Column(name = "etat")
    @Enumerated(EnumType.STRING)
    private EEtatAutorisation etat = EEtatAutorisation.EN_ATTENTE;

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaDocumentAutorisationSpecial> documents = new HashSet<>();

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaDelegationMembre> delegation = new HashSet<>();
}
