package com.portfolio.study_management_app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
  @Mock
  CategoryRepository categoryRepository;
  @Mock
  UserRepository userRepository;

  @InjectMocks
  CategoryService categoryService;

  @Test
  @DisplayName("正常系:Category登録成功")
  void createCategory_success() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Category parentCategory = new Category("parent", user, null);

    parentCategory.setCategoryId(10L);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // リクエスト準備
    CreateCategoryRequestDto req = new CreateCategoryRequestDto("test", parentCategory.getCategoryId());

    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    when(categoryRepository.findById(anyLong()))
        .thenReturn(Optional.of(parentCategory));
    when(categoryRepository.save(any(Category.class)))
        .thenAnswer(invocation -> {
          Category c = invocation.getArgument(0);
          c.setCategoryId(1L);
          return c;
        });

    // 実行
    CategoryResponseDto result = categoryService.createCategory(req);

    // 保存するデータを取得
    ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

    verify(categoryRepository).save(captor.capture());

    Category baforeSavingCategory = captor.getValue();

    // 保存したデータの検証
    assertNotNull(baforeSavingCategory.getCategoryId());
    assertEquals("test", baforeSavingCategory.getCategoryName());
    assertNotNull(baforeSavingCategory.getCreatedAt());
    assertEquals(true, baforeSavingCategory.isStatus());
    assertEquals(user, baforeSavingCategory.getUser());
    assertEquals(parentCategory, baforeSavingCategory.getParentCategory());

    // 戻り値の検証
    assertNotNull(result.categoryId());
    assertEquals("test", result.categoryName());
  }

  @Test
  @DisplayName("異常系: parentCategoryIdが存在しない場合Category登録失敗")
  void faild_createCategory_By_parentCategory_notFound() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Category parentCategory = new Category("parent", user, null);

    parentCategory.setCategoryId(10L);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // リクエストの準備
    CreateCategoryRequestDto req = new CreateCategoryRequestDto("test", 2L);

    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    when(categoryRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    // 実行・エラーの確認
    assertThrows(ValidationException.class,
        () -> categoryService.createCategory(req));

    // 不要な挙動がないか確認
    verify(categoryRepository, never()).save(any(Category.class));
  }

  @Test
  @DisplayName("正常系: List<Category>取得成功")
  void getCategoriesByUserId() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // 前提条件のセット
    Category parentCategory = new Category("parent", user, null);

    Category childCategory = new Category("child", user, parentCategory);

    Category grandChildCategory = new Category("grandChild", user, childCategory);

    parentCategory.getChildren().add(childCategory);

    childCategory.getChildren().add(grandChildCategory);

    List<Category> categories = List.of(parentCategory, childCategory, grandChildCategory);

    // 実行準備
    when(categoryRepository.findByUserUserId(anyLong()))
        .thenReturn(categories);

    // 実行
    List<CategoryResponseDto> result = categoryService.getCategoriesByUserId();

    // 取得データの整理
    CategoryResponseDto resultParent = result.get(0);
    CategoryResponseDto resultChild = resultParent.children().get(0);
    CategoryResponseDto resultGrandChild = resultChild.children().get(0);

    // データの検証
    assertEquals("parent", resultParent.categoryName());
    assertEquals(1, resultParent.children().size());

    assertEquals("child", resultChild.categoryName());
    assertEquals(1, resultChild.children().size());

    assertEquals("grandChild", resultGrandChild.categoryName());
    assertTrue(resultGrandChild.children().isEmpty());
  }
}
