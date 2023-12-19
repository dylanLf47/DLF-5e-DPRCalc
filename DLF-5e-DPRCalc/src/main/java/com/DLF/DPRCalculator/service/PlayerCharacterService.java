package com.DLF.DPRCalculator.service;

import com.DLF.DPRCalculator.controller.Calculator;
import com.DLF.DPRCalculator.model.PlayerCharacter;

import java.util.List;

public interface PlayerCharacterService {
    public PlayerCharacter savePlayerCharacter(PlayerCharacter playerCharacter);
    public List<PlayerCharacter> getAllPlayerCharacters();
    public void removePlayerCharacter(Integer id);
    public String calculate(PlayerCharacter pc, int ac);
    public PlayerCharacter getPlayerCharacter(Integer id);
}
