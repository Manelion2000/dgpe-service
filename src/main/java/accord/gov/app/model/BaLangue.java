package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "ba_langue")
public class BaLangue extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle", nullable = false,unique = true)
    private String libelle;
}
