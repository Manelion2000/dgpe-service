package accord.gov.app.repositories;

import accord.gov.app.model.BaProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaProductRepository extends JpaRepository<BaProduct,String> {
    boolean existsById(String id);

    List<BaProduct> findByCategorieId(String id);
}
