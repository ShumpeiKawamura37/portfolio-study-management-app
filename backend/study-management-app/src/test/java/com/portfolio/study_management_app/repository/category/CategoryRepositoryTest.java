package com.portfolio.study_management_app.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {
  
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryRepository categoryRepository;

  @Test
  @DisplayName("正常系: userIdでList<Category>取得")
  void findByUserUserId_success() {
    // 前提条件をセット
    User user = new User(
      "山田太郎",
      "test@example.com",
      "Password123"
    );
    userRepository.save(user);

    User savedUser = userRepository.findByEmail("test@example.com");

    Long userId = savedUser.getUserId();

    Category category = new Category(
      "test",
      user, 
      null
    );

    categoryRepository.save(category);

    List<Category> result = categoryRepository.findByUserUserId(userId);
                
    assertEquals("test", result.get(0).getCategoryName());
    assertEquals(savedUser, result.get(0).getUser());
  }
  
}
