package com.portfolio.study_management_app.service.category;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

import jakarta.validation.ValidationException;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
    this.categoryRepository = categoryRepository;
    this.userRepository = userRepository;
  }

  private CategoryResponseDto toDto(Category category) {
    List<CategoryResponseDto> children = category.getChildren()
        .stream()
        .map(this::toDto)
        .toList();

    return new CategoryResponseDto(
        category.getCategoryId(),
        category.getCategoryName(),
        children);
  }

  private List<CategoryResponseDto> toTree(List<Category> categories) {

    return categories.stream()
        .filter(category -> category.getParentCategory() == null)
        .map(this::toDto)
        .toList();
  }

  public CategoryResponseDto createCategory(CreateCategoryRequestDto req) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));

    Category category = new Category(req.categoryName(), user, null);

    if (req.parentCategoryId() != null) {
      Category parentCategory = categoryRepository.findById(req.parentCategoryId())
          .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));
      parentCategory.addChild(category);
    }

    Category savedCategory = categoryRepository.save(category);

    return new CategoryResponseDto(
        savedCategory.getCategoryId(),
        savedCategory.getCategoryName(),
        null);
  }

  public List<CategoryResponseDto> getCategoriesByUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    List<Category> categories = categoryRepository.findByUserUserId(userId);

    List<CategoryResponseDto> tree = this.toTree(categories);
    return tree;
  }
}
