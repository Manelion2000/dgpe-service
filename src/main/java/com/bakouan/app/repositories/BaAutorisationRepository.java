package com.bakouan.app.repositories;

import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.EStatut;
import com.bakouan.app.model.BaAutorisationSpeciale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaAutorisationRepository extends JpaRepository<BaAutorisationSpeciale, String> {
    List<BaAutorisationSpeciale> findByUserId(String userId);
    List<BaAutorisationSpeciale> findByStatut(EStatut eStatut);

    List<BaAutorisationSpeciale> findByEtat(EEtatAutorisation etatAutorisation);

    @Query("SELECT COUNT(a) FROM BaAutorisationSpeciale a WHERE EXTRACT(YEAR FROM a.dateDemande) = :year")
    long countByCurrentYear(@Param("year") int year);

    @Query("SELECT a.numDemande FROM BaAutorisationSpeciale a WHERE a.numDemande LIKE CONCAT('BF-AU-', :year, '-%') ORDER BY a.numDemande DESC")
    List<String> findLastNumeroDemandeForYear(@Param("year") String year);


}
