package com.example.lab4.model;

import com.example.lab4.entity.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString
@EqualsAndHashCode
public class User implements UserDetails {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private boolean accountNonExpired = true;
    @Getter
    @Setter
    private boolean accountNonLocked = true;
    @Getter
    @Setter
    private boolean credentialsNonExpired = true;
    @Getter
    @Setter
    private boolean enabled = true;

    public static User toModel(UserEntity entity) {
        User result = new User();
        result.setUsername(entity.getUsername());
        result.setPassword(entity.getPassword());
        result.setAccountNonExpired(entity.isNonExpired());
        result.setAccountNonLocked(entity.isNonLocked());
        result.setCredentialsNonExpired(entity.isCredentialsNonExpired());
        result.setEnabled(entity.isEnabled());
        return result;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }
}
