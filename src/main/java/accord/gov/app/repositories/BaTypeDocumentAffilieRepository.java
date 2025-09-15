package accord.gov.app.repositories;

import accord.gov.app.model.BaTypeDocumentAffilie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 06/09/2025 à 03:20
 */
@Repository
public interface BaTypeDocumentAffilieRepository extends JpaRepository<BaTypeDocumentAffilie,String > {
    Optional<BaTypeDocumentAffilie> findByLibelle(String libelle);
}
