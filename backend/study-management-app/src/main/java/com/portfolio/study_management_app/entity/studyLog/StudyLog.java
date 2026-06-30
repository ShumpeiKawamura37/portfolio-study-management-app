package com.portfolio.study_management_app.entity.studyLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "study_logs")
public class StudyLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "study_log_id")
  private Long studyLogId;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @Column(nullable = false)
  private Integer studySeconds;

  @Column(nullable = true)
  @Size(max = 1000)
  private String memo;

  @NotNull
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  protected StudyLog() {}

  public StudyLog(LocalDateTime starTime, LocalDateTime endTime, Integer studySeconds, String memo, User user, Category category) {
    this.startTime = starTime;
    this.endTime = endTime;
    this.studySeconds = studySeconds;
    this.memo = memo;
    this.user = user;
    this.category = category;
    this.createdAt = LocalDateTime.now();
  }
}