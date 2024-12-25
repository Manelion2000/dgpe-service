package com.bakouan.app.repositories;
import com.bakouan.app.model.BaDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaDocumentRepository extends JpaRepository<BaDocument,String> {
    boolean existsById(String id);
    /**Fonction de recuperation de la liste
     des documents par demande
     @param idDemande
     */
    List<BaDocument> findByDemandeId(String idDemande);

}
