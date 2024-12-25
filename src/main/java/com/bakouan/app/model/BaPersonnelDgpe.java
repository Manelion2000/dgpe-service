package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "ba_pesonnel_dgpe")
public class BaPersonnelDgpe extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "url")
    private String url;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "paragraphe1")
    private String paragraphe1;

    @Column(name = "paragraphe2")
    private String paragraphe2;

    @Column(name = "paragraphe3")
    private String paragraphe3;

    @Column(name = "paragraphe4")
    private String paragraphe4;

}
