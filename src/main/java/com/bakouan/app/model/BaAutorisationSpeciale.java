package com.bakouan.app.model;

import com.bakouan.app.dto.BaDocumentAutorisationSpecialDto;
import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.ETypeAutorisation;
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

    @Column(name="numero")
    private String numDemande;

    @Column(name = "date_demande", unique = true)
    private LocalDate dateDemande;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "motif")
    private String motifRejet;

    @Column(name ="date_rejet")
    private LocalDate dateRejet;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(name = "etat")
    @Enumerated(EnumType.STRING)
    private EEtatAutorisation etat;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_autorisation")
    private ETypeAutorisation typeAutorisation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaUser user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_final_id", referencedColumnName = "id")
    private BaDocumentAutorisationSpecial documentFinal;

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaDocumentAutorisationSpecial> documents = new HashSet<>();

    @OneToMany(mappedBy = "autorisationSpeciale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaDelegationMembre> delegation = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="mission_diplomatique_id", nullable = true)
    private BaMissionDiplomatique missionDiplomatique;
}
