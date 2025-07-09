package accord.gov.app.model;

import accord.gov.app.enums.EConfidentiel;
import accord.gov.app.enums.ENatureDocument;
import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 18:50
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ba_document")
public class BaDocument extends BaAbstractAuditingEntity {

    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle", unique = true, nullable = false)
    private String libelle;

    @Column(name = "note")
    private String note;

    @Column(name = "mot_cle")
    private String motCle;

    @Column(name = "resume", columnDefinition = "TEXT")
    private String resume;

    @Column(name = "code_boite")
    private Integer codeBoite;

    @Column(name = "etat_accord_vigueur")
    private String etatAccordVigueur;

    @Column(name = "confidentialite")
    @Enumerated(EnumType.STRING)
    private EConfidentiel confidentialite=EConfidentiel.NON;

    @Column(name = "date_adoption")
    private LocalDate dateAdoption;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Column(name = "date_entree_vigueur")
    private LocalDate dateEntreeVigueur;

    @Column(name = "date_ratification")
    private LocalDate dateRatification;

    @Column(name = "chemin_fichier")
    private String cheminFichier;

    @Column(name = "lieu_signature")
    private String lieuSignature;

    @Column(name="nature_document")
    @Enumerated(EnumType.STRING)
    private ENatureDocument natureDocument;

    @ManyToOne
    @JoinColumn(name = "langue_id")
    private BaLangue langue;

    @ManyToOne
    @JoinColumn(name = "type_document_id")
    private BaTypeAccord typeDocument;

    @ManyToMany
    @JoinTable(
            name = "ba_document_partie",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "partie_id")
    )
    private List<BaPartie> partiesPrenantes = new ArrayList<>();

    @OneToMany(mappedBy = "documentPrincipal", cascade = CascadeType.ALL)
    private List<BaDocumentAffilie> documentsAffilies = new ArrayList<>();
}

