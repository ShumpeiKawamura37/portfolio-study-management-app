package com.portfolio.study_management_app.controller.category;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;
import com.portfolio.study_management_app.service.category.CategoryService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerApiTest {
  @Autowired MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private JwtProvider jwtProvider;

  @Test
  @DisplayName("正常系: Cagtegory作成成功")
  void createCategory_success() throws Exception {
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);

    Category parentCategory = new Category("parent", user, null);

    Category savedParentCategory = categoryRepository.save(parentCategory);
    
    String token = jwtProvider.generateToken(savedUser.getUserId());

    String json = """
      {
        "categoryName":"test",
        "parentCategoryId": %d
      }
    """.formatted(savedParentCategory.getCategoryId());

    mockMvc.perform(
      post("/api/category")
        .header("Authorization", "Bearer " + token)
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("SUCCESS"))
      .andExpect(jsonPath("$.data.categoryId").exists())
      .andExpect(jsonPath("$.data.categoryName").value("test"));
  }
}
