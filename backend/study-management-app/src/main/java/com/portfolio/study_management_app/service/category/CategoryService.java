package com.portfolio.study_management_app.service.category;

import java.util.ArrayList;
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

  // JWTから設定された認証情報のuserIdを取得
  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (Long) authentication.getPrincipal();
  }

  // dtoに変換
  private CategoryResponseDto toDto(Category category) {
    List<CategoryResponseDto> children = category.getChildren()
        .stream()
        .filter(Category::isStatus)
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

  //categoryを論理削除し、所属するログは物理削除する
  private void deleteCategoryAndStudyLogRecursively(Category category) {
      studyLogRepository.deleteByCategoryCategoryId(category.getCategoryId());
      category.setStatus(false);

      for (Category child : category.getChildren()) {
          deleteCategoryAndStudyLogRecursively(child);
      }
  }

  // 対象の祖先要素リストを取得　再帰処理で遡る
  private List<Category> addParentCategory(List<Category> ancestorCategoryList, Category targetCategory) {
    if(targetCategory.getParentCategory() != null) {
        ancestorCategoryList.add(targetCategory.getParentCategory());
        addParentCategory(ancestorCategoryList, targetCategory.getParentCategory());
      }

    return ancestorCategoryList;
  }

  // Category作成
  public CategoryResponseDto createCategory(CreateCategoryRequestDto req) {
    Long userId = getCurrentUserId();

    // userが見つからなければエラー
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));

    Category category = new Category(req.categoryName(), user, null);

    // 親要素があるなら、親要素に子要素を、子要素に親要素を加える。
    if (req.parentCategoryId() != null) {
      Category parentCategory = categoryRepository.findByCategoryIdAndUserUserIdAndStatusTrue(req.parentCategoryId(), userId)
          .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));
      parentCategory.addChild(category);
    }

    Category savedCategory = categoryRepository.save(category);

    return toDto(savedCategory);
  }

  // ユーザーが所有するカテゴリ一覧を取得
  public List<CategoryResponseDto> getCategoriesByUserId() {

    Long userId = getCurrentUserId();

    List<Category> categories = categoryRepository.findByUserUserIdOrderByCreatedAtAsc(userId)
        .stream().filter((category) -> {
          return category.isStatus() == true;
        }).toList();

    List<CategoryResponseDto> tree = this.toTree(categories);
    return tree;
  }

  // カテゴリ名を更新
  public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto req) {
    Long userId = getCurrentUserId();

    Category target = categoryRepository.findByCategoryIdAndUserUserIdAndStatusTrue(categoryId, userId)
        .orElseThrow(() -> new ValidationException("データの更新に失敗しました。"));

    target.setCategoryName(req.categoryName());
    Category updatedCategory = categoryRepository.save(target);

    return toDto(updatedCategory);
  }


  // カテゴリ削除　カテゴリの論理削除・対象カテゴリを保有するstudyLogの物理削除を一括で行うため@Transactional 
  @Transactional
  public void deleteCategory(Long categoryId) {
    Long userId = getCurrentUserId();
    
    Category target = categoryRepository.findByCategoryIdAndUserUserIdAndStatusTrue(categoryId, userId)
        .orElseThrow(() -> new ValidationException("データの更新に失敗しました。"));

    studyLogRepository.deleteByCategoryCategoryId(target.getCategoryId());
    
    //entityにCascadeType:ALLが付いているので子・孫要素も同時に削除される。
    deleteCategoryAndStudyLogRecursively(target);

    categoryRepository.save(target);
    return;
  }

  // 対象の祖先要素リストを取得
  public List<CategoryResponseDto> getAncestoCategoryList(Long categoryId) {
    Long userId = getCurrentUserId();

    List<Category> ancestorCategoryList = new ArrayList<Category>();

    Category target = categoryRepository.findByCategoryIdAndUserUserIdAndStatusTrue(categoryId, userId)
      .orElseThrow();

    if(target.getParentCategory() != null) {
      addParentCategory(ancestorCategoryList, target);
    }

    List<CategoryResponseDto> res = ancestorCategoryList.stream().map(ancestorCategory -> {
      return toDto(ancestorCategory);
    }).toList();

    return res;
  }
}
