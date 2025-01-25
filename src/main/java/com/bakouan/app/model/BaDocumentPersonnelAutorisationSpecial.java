package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;

public class BaDocumentPersonnelAutorisationSpecial extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "autorisation_personnel_speciale_id")
    private BaDelegationMembre membre;
}
