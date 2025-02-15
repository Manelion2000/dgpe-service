package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "ba_mission_diplomatique")
public class BaMissionDiplomatique extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id= BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "pays")
    private String pays;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "reciprocite")
    private Boolean reciprocite=false;

    @OneToMany(mappedBy = "missionDiplomatique", cascade =CascadeType.ALL )
    private Set<BaDemande> demandes=new HashSet<>();
}
