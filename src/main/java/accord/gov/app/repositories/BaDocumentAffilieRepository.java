package accord.gov.app.repositories;
import accord.gov.app.model.BaDocumentAffilie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 19:19
 */
@Repository
public interface BaDocumentAffilieRepository extends JpaRepository<BaDocumentAffilie, String> {

}
