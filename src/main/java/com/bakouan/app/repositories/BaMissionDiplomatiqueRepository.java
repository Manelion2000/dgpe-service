package com.bakouan.app.repositories;

import com.bakouan.app.model.BaMissionDiplomatique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaMissionDiplomatiqueRepository extends JpaRepository<BaMissionDiplomatique,String> {
}
