package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);
}
