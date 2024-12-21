package com.join.lesson.feature.uploadfile.repository.web;

import com.join.lesson.core.entity.web.UploadFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFileEntity, Long> {
}
