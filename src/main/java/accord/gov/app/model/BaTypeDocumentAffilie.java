package accord.gov.app.model;

import accord.gov.app.utils.BaUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 06/09/2025 à 02:19
 */
public class BaTypeDocumentAffilie extends BaAbstractAuditingEntity{
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "typeDocumentAffilie")
    private List<BaDocumentAffilie> documentAffilies = new ArrayList<>();

}
