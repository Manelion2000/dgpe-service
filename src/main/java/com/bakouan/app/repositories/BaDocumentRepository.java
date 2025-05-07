package com.bakouan.app.repositories;
import com.bakouan.app.enums.EDocument;
import com.bakouan.app.model.BaDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaDocumentRepository extends JpaRepository<BaDocument,String> {
    boolean existsById(String id);
    /**Fonction de recuperation de la liste
     des documents par demande
     @param idDemande
     */
    List<BaDocument> findByDemandeId(String idDemande);

    //List<BaDocument> findByTypeDocumentAndDemandeId(EDocument typeDocument, String demandeId);

    Optional<BaDocument> findByTypeDocumentAndDemandeId(EDocument typeDocument, String demandeId);



}
