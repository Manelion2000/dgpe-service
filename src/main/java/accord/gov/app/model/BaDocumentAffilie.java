package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 18:51
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ba_document_affilie")
public class BaDocumentAffilie extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "intitule_affilie")
    private String intituleAffilie;

    @Column(name = "code_boite")
    private String codeBoite;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Column(name = "date_ratification")
    private LocalDate dateRatification;

    @Column(name = "libelle_fichier", unique = true)
    private String libelleFichier;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "document_principal_id")
    private BaDocument accord;

    @ManyToOne
    @JoinColumn(name="type_affilie_id",nullable = false)
    private BaTypeDocumentAffilie typeDocumentAffilie;

    @OneToMany(mappedBy = "affilie", cascade = CascadeType.ALL)
    private Set<BaFichier> fichiers = new HashSet<>();

    public void addFichier(BaFichier fichier) {
        fichiers.add(fichier);
        fichier.setAffilie(this);
    }

    public void removeFichier(BaFichier fichier) {
        fichiers.remove(fichier);
        fichier.setAccord(null);
    }
}

