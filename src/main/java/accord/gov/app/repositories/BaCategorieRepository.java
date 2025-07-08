package accord.gov.app.repositories;

import accord.gov.app.enums.EStatut;
import accord.gov.app.model.BaCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaCategorieRepository extends JpaRepository<BaCategorie, String> {

    Boolean existsByCode(String code);

    List<BaCategorie> findByStatut(EStatut eStatus)
            ;
}
