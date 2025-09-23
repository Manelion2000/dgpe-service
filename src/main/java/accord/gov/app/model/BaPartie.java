package accord.gov.app.model;

import accord.gov.app.enums.ETypePartie;
import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 18:52
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ba_partie")
public class BaPartie extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @Column(name="sigle", nullable =true)
    private String sigle;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_partie", nullable = false)
    private ETypePartie typePartie;
}
