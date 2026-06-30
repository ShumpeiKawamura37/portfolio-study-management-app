package com.portfolio.study_management_app.controller.category;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerApiTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private JwtProvider jwtProvider;
  @Test
  @DisplayName("正常系: Cagtegory登録成功")
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

  @Test
  @DisplayName("異常系: parentCategoryIdが存在しない場合Category登録失敗")
  void faild_createCategory_By_parentCategory_notFound() throws Exception {
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);

    String token = jwtProvider.generateToken(savedUser.getUserId());

    String json = """
          {
            "categoryName":"test",
            "parentCategoryId": "1L"
          }
        """;
    mockMvc.perform(
        post("/api/category")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("正常系: List<Category>取得成功")
  void getCategoriesByUserId_success() throws Exception {
    // 条件セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);

    Category parentCategory = new Category("parent", savedUser, null);

    Category childCategory = new Category("child", savedUser, parentCategory);

    Category grandChildCategory = new Category("grandChild", savedUser, childCategory);

    parentCategory.addChild(childCategory);
    childCategory.addChild(grandChildCategory);

    categoryRepository.save(parentCategory);
    categoryRepository.save(childCategory);
    categoryRepository.save(grandChildCategory);

    String token = jwtProvider.generateToken(savedUser.getUserId());

    // 実行
    mockMvc.perform(
        get("/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .with(csrf()))
        .andExpect(jsonPath("$.status").value("SUCCESS"))
        .andExpect(jsonPath("$.data[0].categoryName")
            .value("parent"))
        .andExpect(jsonPath("$.data[0].children[0].categoryName")
            .value("child"))
        .andExpect(jsonPath("$.data[0].children[0].children[0].categoryName")
            .value("grandChild"));
  }

  @Test
  @DisplayName("正常系: Category更新成功")
  void updateCategory_success()  throws Exception{
    // 条件セット
    User user = new User("test", "test@exapmle.com", "Password123");

    User savedUser = userRepository.save(user);

    Category category = new Category("test", user, null);
    categoryRepository.save(category);

    String token = jwtProvider.generateToken(savedUser.getUserId());

    String json = """
      {
        "categoryName":"updated"
      }
    """;
    // 実行
    mockMvc.perform(
      put("/api/category/{categoryId}", category.getCategoryId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .header("Authorization", "Bearer " + token)
        .with(csrf()))
      .andExpect(jsonPath("$.status").value("SUCCESS"))
      .andExpect(jsonPath("$.data.categoryId").value(category.getCategoryId()))
      .andExpect(jsonPath("$.data.categoryName").value("updated"));
  }

  @Test
  @DisplayName("異常系: カテゴリが見つからなければCategory更新失敗")
  void update_faild_categoryId_not_found() throws Exception {
     // 条件セット
    User user = new User("test", "test@exapmle.com", "Password123");

    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

    String json = """
      {
        "categoryName":"updated"
      }
    """;
    // 実行
    mockMvc.perform(
      put("/api/category/{categoryId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .header("Authorization", "Bearer " + token)
        .with(csrf()))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("正常系: Category削除成功")
  void deleteCategory_success() throws Exception {
     // 条件セット
    User user = new User("test", "test@exapmle.com", "Password123");

    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());
    
     // 実行
    mockMvc.perform(
      put("/api/category/{categoryId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .with(csrf()))
      .andExpect(status().isBadRequest());
  }
  @Test
  @DisplayName("異常系: カテゴリが見つからなければCategory削除失敗")
  void delete_failed_categoryId_not_found() throws Exception {
      // 条件セット
    User user = new User("test", "test@exapmle.com", "Password123");

    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());
    mockMvc.perform(
    put("/api/category/{categoryId}", 1L)
      .contentType(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer " + token)
      .with(csrf()))
    .andExpect(status().isBadRequest());
  }
}
