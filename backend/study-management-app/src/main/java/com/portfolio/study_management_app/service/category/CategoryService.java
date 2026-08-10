package com.portfolio.study_management_app.service.category;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.category.CategoryRequestDto;
import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
    this.categoryRepository = categoryRepository;
    this.userRepository = userRepository;
  }

  // dtoに変換
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

  // 子要素全てをツリー構造のDTOに変換
  private List<CategoryResponseDto> toTree(List<Category> categories) {

    return categories.stream()
        .filter(category -> category.getParentCategory() == null)
        .map(this::toDto)
        .toList();
  }

  // Category作成
  public CategoryResponseDto createCategory(CreateCategoryRequestDto req) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    // userが見つからなければエラー
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));

    Category category = new Category(req.categoryName(), user, null);

    // 親要素があるなら、親要素に子要素を、子要素に親要素を加える。
    if (req.parentCategoryId() != null) {
      Category parentCategory = categoryRepository.findById(req.parentCategoryId())
          .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));
      parentCategory.addChild(category);
    }

    Category savedCategory = categoryRepository.save(category);

    return toDto(savedCategory);
  }

  public List<CategoryResponseDto> getCategoriesByUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    List<Category> categories = categoryRepository.findByUserUserIdOrderByCreatedAtAsc(userId)
        .stream().filter((category) -> {
          return category.isStatus() == true;
        }).toList();

    List<CategoryResponseDto> tree = this.toTree(categories);
    return tree;
  }

  public CategoryResponseDto updateCategory(Long categroyId, CategoryRequestDto req) {
    Category target = categoryRepository.findById(categroyId)
        .orElseThrow(() -> new ValidationException("データの更新に失敗しました。"));
    target.setCategoryName(req.categoryName());
    Category updatedCategory = categoryRepository.save(target);

    return toDto(updatedCategory);
  }

  public void deleteCategory(Long categoryId) {
    Category target = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ValidationException("データの更新に失敗しました。"));

    // 論理削除して保存
    target.setStatus(false);

    categoryRepository.save(target);
    return;
  }
}
