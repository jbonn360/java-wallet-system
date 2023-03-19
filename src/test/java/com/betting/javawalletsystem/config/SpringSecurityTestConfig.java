package com.betting.javawalletsystem.config;

import com.betting.javawalletsystem.model.Player;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@TestConfiguration
public class SpringSecurityTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        Collection<GrantedAuthority> userAuthorities = new ArrayList();
        userAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User user = new User("lucky", "mypassword123", userAuthorities);

        Collection<GrantedAuthority> adminAuthorities = new ArrayList();
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User admin = new User("admin", "admin", adminAuthorities);

        return new InMemoryUserDetailsManager(Arrays.asList(
                user, admin
        ));
    }
}
