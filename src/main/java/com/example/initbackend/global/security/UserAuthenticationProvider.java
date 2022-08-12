package com.example.initbackend.global.security;

import com.example.initbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.jar.JarFile;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = (String)authentication.getPrincipal();
        String password =(String)authentication.getCredentials();

        UserDetails userDetails = userService.loadUserByUsername(email);

        if (!checkPassword(password, userDetails.getPassword()) || !userDetails.isEnabled()) {
            throw new BadCredentialsException(email);
        }

        if(!userDetails.isEnabled()){
            throw new BadCredentialsException(email);
        }

        return new UsernamePasswordAuthenticationToken(userDetails , null, userDetails.getAuthorities());

    }
    @Override public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    private boolean checkPassword(String loginPassword, String dbPassword) {

        return loginPassword.equals(dbPassword);
    }
}

