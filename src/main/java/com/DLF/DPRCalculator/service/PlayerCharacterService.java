package com.DLF.DPRCalculator.service;

import com.DLF.DPRCalculator.controller.Calculator;
import com.DLF.DPRCalculator.model.PlayerCharacter;

import java.util.List;

public interface PlayerCharacterService {
    public PlayerCharacter savePlayerCharacter(PlayerCharacter playerCharacter);
    public List<PlayerCharacter> getAllPlayerCharacters();
    public List<PlayerCharacter> getPlayerCharactersByUsername(String username);
    public void removePlayerCharacter(Integer id);
    public void removePlayerCharacterByUsername(String username);
    public String calculate(PlayerCharacter pc, int ac);
    public PlayerCharacter getPlayerCharacter(Integer id);
}
