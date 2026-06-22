package com.portfolio.study_management_app.repository.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.portfolio.study_management_app.entity.user.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

        @Autowired private UserRepository userRepository;

        @Test
        @DisplayName("正常系: emailでユーザー取得")
        void findByEmail_success() {

                User user = new User(
                        "山田太郎",
                        "test@example.com",
                        "Password123");

                user.setCreatedAt(LocalDateTime.now());
                user.setStatus(true);

                userRepository.save(user);

                User result =
                        userRepository.findByEmail("test@example.com");

                assertEquals("山田太郎", result.getUsername());
                assertEquals("test@example.com", result.getEmail());   
        }

        @Test
        @DisplayName("異常系: 存在しないemail")
        void findByEmail_notFound() {

                User result =
                        userRepository.findByEmail("notfound@example.com");

                assertNull(result);
        }
}