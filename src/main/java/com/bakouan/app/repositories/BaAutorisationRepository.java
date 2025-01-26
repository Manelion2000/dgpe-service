package com.bakouan.app.repositories;

import com.bakouan.app.model.BaAutorisationSpeciale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaAutorisationRepository extends JpaRepository<BaAutorisationSpeciale, String> {
}
