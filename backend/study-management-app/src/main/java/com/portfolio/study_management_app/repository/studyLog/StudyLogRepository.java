package com.portfolio.study_management_app.repository.studyLog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.study_management_app.entity.studyLog.StudyLog;

@Repository
public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
  public List<StudyLog> findByUserUserId(Long userId);
  public List<StudyLog> findByCategoryCategoryId(Long categoryId);
  public List<StudyLog> findByUserUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan(Long userId, LocalDateTime start, LocalDateTime end);
} 