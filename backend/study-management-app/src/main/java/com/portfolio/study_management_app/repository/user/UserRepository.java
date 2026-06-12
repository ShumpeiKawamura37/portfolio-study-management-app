package com.portfolio.study_management_app.repository.user;

import org.springframework.stereotype.Repository;

import com.portfolio.study_management_app.entity.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByEmail(String email);
}
