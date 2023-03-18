package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.exception.UserNotFoundException;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PlayerRepository playerRepository;

    public UserDetailsServiceImpl(@Autowired PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(String.format("User with username %s was not found", username))
        );

        return new User(player.getUsername(), player.getPassword(), getGrantedAuthority(player));
    }

    private Collection<GrantedAuthority> getGrantedAuthority(Player player){
        Collection<GrantedAuthority> authorities = new ArrayList();

        if(player.getRole().isAdmin()){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
