package com.portfolio.study_management_app.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.study_management_app.entity.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  public List<Category> findByUserUserIdByCreatedAtAsc(Long userId);
} 
