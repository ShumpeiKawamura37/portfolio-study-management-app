package com.portfolio.study_management_app.integration.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.portfolio.study_management_app.dto.category.CategoryRequestDto;
import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.service.category.CategoryService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class IntegrationCategoryTest {
  @Autowired
  UserRepository userRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  CategoryService categoryService;

  @Test
  @DisplayName("正常系: Category登録成功")
  void createCategory_success() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Category parentCategory = new Category("parent", user, null);

    parentCategory.setCategoryId(10L);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    
    // リクエスト準備
    CreateCategoryRequestDto req = new CreateCategoryRequestDto("test", null);

    CategoryResponseDto result = categoryService.createCategory(req);

    Category savedCategory = categoryRepository.findById(result.categoryId())
        .orElseThrow();

    assertNotNull(savedCategory);
    assertEquals("test", savedCategory.getCategoryName());
  }

  @Test
  @DisplayName("異常系: parentCategoryIdが存在しない場合Category登録失敗")
  void faild_createCategory_By_parentCategory_notFound(){
    // 認証セット
    User user = new User("test", "test@example.com", "Password123");

    userRepository.save(user);
    
    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth); 

    // リクエストの準備
    CreateCategoryRequestDto req = new CreateCategoryRequestDto("test", 99L);

    // 実行・エラーの確認
    assertThrows(ValidationException.class,
        () -> categoryService.createCategory(req));
  }

  @Test
  @DisplayName("正常系: List<Catefgory>取得成功")
  void getCategoryByUserId_success() {
    // 認証セット
    User user = new User("test", "test@example.com", "Password123");

    userRepository.save(user);
    
    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth); 
    
    Category parentCategory = new Category("parent", user, null);

    Category childCategory = new Category("child", user, parentCategory);

    Category grandChildCategory = new Category("grandChild", user, childCategory);

    parentCategory.addChild(childCategory);
    childCategory.addChild(grandChildCategory);

    categoryRepository.save(parentCategory);
    categoryRepository.save(childCategory);
    categoryRepository.save(grandChildCategory);

    List<CategoryResponseDto> result = categoryService.getCategoriesByUserId();

    CategoryResponseDto resultParent = result.get(0);
    CategoryResponseDto resultChild = resultParent.children().get(0);
    CategoryResponseDto resultGrandChild = resultChild.children().get(0);

    assertEquals("parent", resultParent.categoryName());
    assertEquals(1, resultParent.children().size());

    assertEquals("child", resultChild.categoryName());
    assertEquals(1, resultChild.children().size());

    assertEquals("grandChild", resultGrandChild.categoryName());
    assertTrue(resultGrandChild.children().isEmpty());
  }

  @Test
  @DisplayName("正常系: Category更新成功")
  void updateCategory_success() {
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");

    userRepository.save(user);
    
    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth); 
    
    // 前提条件のセット
    Category category = new Category("test", user, null);

    categoryRepository.save(category);
    
    CategoryRequestDto req = new CategoryRequestDto("updated");

    //実行
    CategoryResponseDto result = categoryService.updateCategory(category.getCategoryId(), req);

    assertEquals(category.getCategoryId(), result.categoryId());
    assertEquals("updated", result.categoryName());
  }

  @Test
  @DisplayName("異常系: カテゴリが見つからなければCategory更新失敗")
  void faild_categoryId_not_found() {
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");

    userRepository.save(user);
    
    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth); 

    CategoryRequestDto req = new CategoryRequestDto("updated");

    //実行
    assertThrows(ValidationException.class,
        () -> categoryService.updateCategory(1L, req));
  }
}
