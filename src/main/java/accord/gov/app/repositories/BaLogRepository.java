package accord.gov.app.repositories;

import accord.gov.app.model.BaLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaLogRepository extends JpaRepository<BaLog, String> {
}
