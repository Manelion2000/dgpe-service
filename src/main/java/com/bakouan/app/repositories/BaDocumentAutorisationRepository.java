package com.bakouan.app.repositories;

import com.bakouan.app.model.BaDocumentAutorisationSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaDocumentAutorisationRepository extends JpaRepository<BaDocumentAutorisationSpecial,String> {
}
