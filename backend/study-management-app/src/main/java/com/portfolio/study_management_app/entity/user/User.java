package com.portfolio.study_management_app.entity.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 100)
    @Pattern(regexp = "^[ぁ-んァ-ヶ一-龯a-zA-Z]+(?: [ぁ-んァ-ヶ一-龯a-zA-Z]+)*$")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$")
    private String password;

    @NotNull  
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean status;

    // Constructors
    protected User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.status = true;
    }

}
