package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.exception.UnauthorisedException;
import com.betting.javawalletsystem.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationServiceImpl implements AuthorisationService{

    private PlayerService playerService;

    public AuthorisationServiceImpl(@Autowired PlayerService playerService){
        this.playerService = playerService;
    }

    @Override
    public void ensurePlayerAuthorised(Long playerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final String currentUserName = authentication.getName();

            Player player = playerService.findPlayerById(playerId);

            result = player.getUsername().equals(currentUserName);
        }

        if(!result)
            throw new UnauthorisedException("Player is not authorised to perform current operation");
    }
}
