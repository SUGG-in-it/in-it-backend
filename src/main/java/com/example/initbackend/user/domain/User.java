package com.example.initbackend.user.domain;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 30)
    private String github_account;

    @Column
    private String introduction;

    @Column
    private String year;

    @Column
    private String work_position;

    @Column
    private String company;

    @Column
    private String career;

    @Column
    private Integer auth_type;

    @ColumnDefault("0")
    private Integer point;

    @Column
    private String level;


    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;


    private String role = "ROLE_USER";

    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;
    public void encryptPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private String authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> auth = new HashSet<>();
        auth.add(new SimpleGrantedAuthority(authority));
        return auth;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }




    @Builder
    public User(String email, String password, String year, String work_position, String nickname, String authority, boolean enabled, String github_account, String introduction, String career, String company, Integer point, String level) {

        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.year = year;
        this.work_position = work_position;
        this.authority = authority;
        this.enabled = enabled;
        this.github_account = github_account;
        this.introduction = introduction;
        this.career= career;
        this.company = company;
        this.point= point;
        this.level = level;

    }


}