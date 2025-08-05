package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 10/07/2025 à 20:21
 */
@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="ba_pays")
public class BaPays extends BaAbstractAuditingEntity{
    @Id
    @Column(name="id")
    private String id = BaUtils.randomUUID();

    @Column(name="libelle")
    private String  libelle;
}
