package com.bakouan.app.model;

import com.bakouan.app.enums.EDocument;
import com.bakouan.app.enums.EDocumentAutorisation;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "ba_photo")
public class BaPhotoPersonnel extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "personnel_id")
    private BaPersonnelDgpe personnel;


}
