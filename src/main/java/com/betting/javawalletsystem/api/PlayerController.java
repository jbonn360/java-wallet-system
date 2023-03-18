package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.WalletDto;
import com.betting.javawalletsystem.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PlayerController {
    private PlayerService playerService;

    public PlayerController(@Autowired PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping(value = "/admin/player/balance", params = { "playerId" }, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public WalletDto getPlayerBalance(@RequestParam("playerId") Long playerId){
        return playerService.getWalletDetails(playerId);
    }
}
