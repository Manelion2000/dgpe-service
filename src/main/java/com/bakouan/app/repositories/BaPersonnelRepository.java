package com.bakouan.app.repositories;

import com.bakouan.app.enums.EStatut;
import com.bakouan.app.model.BaPersonnelDgpe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaPersonnelRepository extends JpaRepository<BaPersonnelDgpe, String> {
    List<BaPersonnelDgpe> findByStatut(EStatut eStatut);
}
