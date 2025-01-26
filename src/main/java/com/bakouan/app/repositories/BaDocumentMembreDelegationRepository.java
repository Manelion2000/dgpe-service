package com.bakouan.app.repositories;

import com.bakouan.app.model.BaDocumentPersonnelAutorisationSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaDocumentMembreDelegationRepository extends JpaRepository<BaDocumentPersonnelAutorisationSpecial, String> {
}
