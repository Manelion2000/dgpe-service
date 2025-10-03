package accord.gov.app.repositories;

import accord.gov.app.model.BaDomaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 19:25
 */
@Repository
public interface BaDomaineRepository extends JpaRepository<BaDomaine,String> {
    long countByIdIn(List<String> ids);
}
