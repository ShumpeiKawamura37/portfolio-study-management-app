package com.portfolio.study_management_app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
  @Mock CategoryRepository categoryRepository;
  @Mock UserRepository userRepository;

  @InjectMocks CategoryService categoryService;

  @Test
  @DisplayName("正常系:Category登録成功")
  void createCategory_success() {
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
    
      CategoryResponseDto result = categoryService.createCategory(req);

      ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

      verify(categoryRepository).save(captor.capture());

      Category savedCategory = captor.getValue();

      // 保存したデータの検証
      assertNotNull(savedCategory.getCategoryId());
      assertEquals("test", savedCategory.getCategoryName());
      assertNotNull(savedCategory.getCreatedAt());
      assertEquals(true, savedCategory.isStatus());
      assertEquals(user, savedCategory.getUser());
      assertEquals(parentCategory, savedCategory.getParentCategory());
      
      // 戻り値の検証
      assertNotNull(result.categoryId());
      assertEquals("test", result.categoryName());
  }

  // @Test
  // @DisplayName("異常系: parentCategoryIdが存在しない場合Category登録失敗")

  @Test
  @DisplayName("正常系: List<Category>取得成功") 
  void getCategoriesByUserId() {
    User user = new User("test", "test@exapmle.com", "password123");
    
    Category parentCategory = new Category("parent", user, null);

    Category childCategory = new Category("child", user, parentCategory);

    Category grandChildCategory = new Category("grandChild", user, childCategory);

    parentCategory.getChildren().add(childCategory);

    childCategory.getChildren().add(grandChildCategory);

    List<Category> categories = List.of(parentCategory, childCategory, grandChildCategory);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    when(categoryRepository.findByUserUserId(anyLong()))
      .thenReturn(categories);
    
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
}
