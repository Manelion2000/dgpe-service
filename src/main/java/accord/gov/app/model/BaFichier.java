package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 14/07/2025 à 13:04
 */
@Entity @AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
@Table(name="ba_fichier")
public class BaFichier extends BaAbstractAuditingEntity {
    @Id
    @Column(name = "id")
    private String id= BaUtils.randomUUID();

    @Column(name = "libelle", unique = true)
    private String libelle;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_principal_id")
    private BaDocument accord;
}
