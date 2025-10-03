package accord.gov.app.repositories;

import accord.gov.app.model.BaLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 19:23
 */
@Repository
public interface BaLangueRepository extends JpaRepository<BaLangue,String> {
    long countByIdIn(List<String> ids);
}
