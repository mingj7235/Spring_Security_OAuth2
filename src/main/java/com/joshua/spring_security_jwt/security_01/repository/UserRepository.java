package com.joshua.spring_security_jwt.security_01.repository;

import com.joshua.spring_security_jwt.security_01.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(final String username);
}
