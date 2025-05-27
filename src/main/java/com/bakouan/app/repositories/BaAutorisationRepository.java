package com.bakouan.app.repositories;

import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.EStatut;
import com.bakouan.app.enums.ETypeAutorisation;
import com.bakouan.app.model.BaAutorisationSpeciale;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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


    List<BaAutorisationSpeciale> findByTypeAutorisation(ETypeAutorisation type);

    /** Met à jour uniquement le champ 'etat' d'une autorisation sans charger toute l'entité.
     * @return le nombre de lignes affectées (0 si id introuvable, 1 sinon)
     */
    @Modifying
    @Transactional
    @Query("UPDATE BaAutorisationSpeciale a SET a.etat = :etat WHERE a.id = :id")
    int updateEtatById(@Param("id") String id, @Param("etat") EEtatAutorisation etat);

}
