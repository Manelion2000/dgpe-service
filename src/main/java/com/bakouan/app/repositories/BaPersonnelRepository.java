package com.bakouan.app.repositories;

import com.bakouan.app.model.BaPersonnelDgpe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaPersonnelRepository extends JpaRepository<BaPersonnelDgpe, String> {
}
