package com.portfolio.study_management_app.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public class UserRepository extends JpaRepository<User, Long> {
  
}
