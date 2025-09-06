package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(name = "titre")
    private String titre;

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
    private BaTypeDocumentAffilie affilie;
}

