package com.example.initbackend.userToken.repository;

import com.example.initbackend.userToken.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface UserTokenRepository  extends JpaRepository<UserToken,String> {

    UserToken findById(Long id);
}
