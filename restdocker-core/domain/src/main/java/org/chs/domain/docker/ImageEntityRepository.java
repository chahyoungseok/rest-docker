package org.chs.domain.docker;

import org.chs.domain.docker.entity.ImageEntity;
import org.chs.domain.docker.querydsl.CustomImageEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, String>, CustomImageEntityRepository {
}