package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 06/09/2025 à 02:19
 */
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "ba_type_document_affilie")
public class BaTypeDocumentAffilie extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle", nullable = false,unique = true)
    private String libelle;

    @OneToMany(mappedBy = "typeDocumentAffilie")
    private List<BaDocumentAffilie> documentAffilies = new ArrayList<>();

}
