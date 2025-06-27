package com.example.oauth2.repository;


import com.example.oauth2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);


    Optional<Users> findByEmailAndPhone(String email, String phone);

}
