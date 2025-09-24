package accord.gov.app.repositories;

import accord.gov.app.enums.EConfidentiel;
import accord.gov.app.enums.EStatut;
import accord.gov.app.model.BaDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 19:19
 */
@Repository
public interface BaDocumentRepository extends JpaRepository<BaDocument, String>, JpaSpecificationExecutor<BaDocument> {

    List<BaDocument> findByStatutAndConfidentialite(EStatut eStatut, EConfidentiel eConfidentiel);
}
