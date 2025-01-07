package com.bakouan.app.repositories;

import com.bakouan.app.model.BaPhotoPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaPhotoPersonnelRepository extends JpaRepository<BaPhotoPersonnel, String> {
}
