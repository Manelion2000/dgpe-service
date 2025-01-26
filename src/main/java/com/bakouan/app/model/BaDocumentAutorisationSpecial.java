package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "ba_document_autorisation_special")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaDocumentAutorisationSpecial extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "autorisation_speciale_id")
    private BaAutorisationSpeciale autorisationSpeciale;
}
