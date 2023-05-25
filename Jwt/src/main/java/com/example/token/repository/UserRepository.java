package com.example.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.token.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	   @EntityGraph(attributePaths = "authorities") // Eager 조회로 authorities를 가져오도록 설정
	   Optional<User> findOneWithAuthoritiesByUsername(String username);
}
