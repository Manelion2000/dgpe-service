package accord.gov.app.repositories;

import accord.gov.app.model.BaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaLogRepository extends JpaRepository<BaLog, String> {
    /**
     * Recuperer la liste des logs pour les documents confidentiels
     * @return une liste de dto de log
     */

    @Query("SELECT l FROM BaLog l " +
            "WHERE l.action = accord.gov.app.enums.EAction.VIEW " +
            "AND LOWER(l.sujet) LIKE LOWER('%consultation du document confidentiel%')")
    List<BaLog> findConfidentialLogs();

}
