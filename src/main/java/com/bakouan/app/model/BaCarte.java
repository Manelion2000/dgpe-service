package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "ba_carte")

public class BaCarte extends BaAbstractAuditingEntity{
    @Column(name="id")
    @Id
    private String id= BaUtils.randomUUID();

    @Column(name="date_production")
    private LocalDate dateProduction;

    @Column(name="date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "code_production", unique = true)
    private String codeProduction;

    @Column(name="code_carte", unique = true)
    private String codeCarte;

    @Column(name="code_barre", unique = true)
    private String codeBarre;

    @ManyToOne
    @JoinColumn(name="ba_carte_id")
    private BaDemande demande;

}
