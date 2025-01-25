package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;

public class BaDocumentAutorisatinSpecial extends BaAbstractAuditingEntity{
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
