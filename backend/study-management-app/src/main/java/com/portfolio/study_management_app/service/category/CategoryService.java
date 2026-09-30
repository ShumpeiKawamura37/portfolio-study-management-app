package com.portfolio.study_management_app.service.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.category.CategoryRequestDto;
import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.studyLog.StudyLogRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;
  private final StudyLogRepository studyLogRepository;

  public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, StudyLogRepository studyLogRepository) {
    this.categoryRepository = categoryRepository;
    this.userRepository = userRepository;
    this.studyLogRepository = studyLogRepository;
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

  //category削除時に、子カテゴリに所属するstudyLogも再帰的に削除する
  private void deleteStudyLogsRecursively(Category category) {

    studyLogRepository.deleteByCategoryCategoryId(
        category.getCategoryId()
    );

    for (Category child : category.getChildren()) {
        deleteStudyLogsRecursively(child);
    }
}

private List<Category> addParentCategory(List<Category> ancestorCategoryList, Category targetCategory) {
  if(targetCategory.getParentCategory() != null) {
      ancestorCategoryList.add(targetCategory.getParentCategory());
      addParentCategory(ancestorCategoryList, targetCategory.getParentCategory());
    }

  return ancestorCategoryList;
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

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category target = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ValidationException("データの更新に失敗しました。"));

    studyLogRepository.deleteByCategoryCategoryId(categoryId);
    deleteStudyLogsRecursively(target);

    // 論理削除して保存
    target.setStatus(false);

    categoryRepository.save(target);
    return;
  }

  public List<CategoryResponseDto> getAncestoCategoryList(Long categoryId) {
    List<Category> ancestorCategoryList = new ArrayList<Category>();

    Category target = categoryRepository.findById(categoryId).orElseThrow();

    if(target.getParentCategory() != null) {
      addParentCategory(ancestorCategoryList, target);
    }

    List<CategoryResponseDto> res = ancestorCategoryList.stream().map(ancestorCategory -> {
      return toDto(ancestorCategory);
    }).toList();

    return res;
  }
}
