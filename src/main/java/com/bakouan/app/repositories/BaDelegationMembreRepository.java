package com.bakouan.app.repositories;

import com.bakouan.app.model.BaDelegationMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaDelegationMembreRepository extends JpaRepository<BaDelegationMembre, String> {
}
