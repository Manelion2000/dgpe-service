package com.bakouan.app.model;

import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "ba_pesonnel_dgpe")
public class BaPersonnelDgpe extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "nom_prenom")
   private String nomPrenom;

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

    @OneToMany(mappedBy = "personnel")
    private Set<BaPhotoPersonnel> documents=new HashSet<>();

}
