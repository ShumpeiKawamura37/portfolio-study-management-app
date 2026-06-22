package com.portfolio.study_management_app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
  @DisplayName("正常系: Userが存在すればCategory登録成功")
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
}
