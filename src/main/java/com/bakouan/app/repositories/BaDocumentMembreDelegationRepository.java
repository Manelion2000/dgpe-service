package com.bakouan.app.repositories;

import com.bakouan.app.model.BaDocumentPersonnelAutorisationSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaDocumentMembreDelegationRepository extends JpaRepository<BaDocumentPersonnelAutorisationSpecial, String> {
    List<BaDocumentPersonnelAutorisationSpecial> findByMembreId(String membreId);
}
