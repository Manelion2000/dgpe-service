package com.bakouan.app.model;

import com.bakouan.app.enums.EDocument;
import com.bakouan.app.enums.EDocumentAutorisation;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "ba_document")
public class BaDocument extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "url")
    private String url;

    @Column(name = "numero_document")
    private String numDocument;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private BaDemande demande;

    @Enumerated(value =EnumType.STRING)
    @Column(name = "type_document")
    private EDocument typeDocument;

    @Enumerated(value =EnumType.STRING)
    @Column(name = "autorisation_speciale")
    private EDocumentAutorisation typeDocumentAutorisation;

}
