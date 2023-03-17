package com.betting.javawalletsystem.creator;

import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Role;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.RoleRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
public class PlayerCreator {
    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;
    private final RoleRepository roleRepository;
    public PlayerCreator(@Autowired PlayerRepository playerRepository,
                         @Autowired WalletRepository walletRepository,
                         @Autowired RoleRepository roleRepository){
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void init(){
        // roles
        Role adminRole = Role.builder()
                .name("admin")
                .build();

        adminRole = roleRepository.save(adminRole);

        Role playerRole = Role.builder()
                .name("player")
                .build();

        playerRole = roleRepository.save(playerRole);

        // player 1
        Wallet playerWallet = Wallet.builder()
                .cashBalance(BigDecimal.ZERO)
                .bonusBalance(BigDecimal.ZERO).build();

        playerWallet = walletRepository.save(playerWallet);

        final String playerPassword = new BCryptPasswordEncoder().encode("mypassword123");
        Player player1 = Player.builder()
                .username("lucky")
                .wallet(playerWallet)
                .password(playerPassword)
                .role(playerRole)
                .build();

        player1 = playerRepository.save(player1);

        // admin player
        Wallet adminWallet = Wallet.builder()
                .cashBalance(BigDecimal.ZERO)
                .bonusBalance(BigDecimal.ZERO).build();

        adminWallet = walletRepository.save(adminWallet);

        final String adminPassword = new BCryptPasswordEncoder().encode("admin");
        Player adminPlayer = Player.builder()
                .username("admin")
                .wallet(adminWallet)
                .password(adminPassword)
                .role(adminRole)
                .build();

        adminPlayer = playerRepository.save(adminPlayer);
    }
}
