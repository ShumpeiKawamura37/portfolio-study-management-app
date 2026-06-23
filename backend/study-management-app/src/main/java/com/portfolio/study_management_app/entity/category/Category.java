package com.portfolio.study_management_app.entity.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.portfolio.study_management_app.entity.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long categoryId;

  @NotNull
  @Column(nullable = false)
  @Size(max = 100)
  private String categoryName;

  @NotNull
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade =  CascadeType.ALL)
  private List<Category> children = new ArrayList<>();

   // Constructors
  protected Category() {}

  public Category(String categoryName,User user, Category parentCategory) {
    this.categoryName = categoryName;
    this.createdAt = LocalDateTime.now();
    this.status = true;
    this.user = user;
    this.parentCategory = parentCategory;
  }

  public void addChild(Category child) {
    children.add(child);
    child.setParentCategory(this);
  }
}
