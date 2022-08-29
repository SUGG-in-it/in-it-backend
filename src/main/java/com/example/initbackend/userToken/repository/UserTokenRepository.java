package com.example.initbackend.userToken.repository;

import com.example.initbackend.user.domain.User;
import com.example.initbackend.userToken.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserTokenRepository  extends JpaRepository<UserToken,String> {
    Optional<UserToken> findByEmail(String email);
}
