package accord.gov.app.repositories;

import accord.gov.app.model.BaFichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 05/08/2025 à 18:28
 */
@Repository
public interface BaFichierRepository extends JpaRepository<String, BaFichier> {
}
