package com.DLF.DPRCalculator.service;

import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;

import java.util.List;

public interface DiceService {
    public Dice saveDice(Dice dice);
    public List<Dice> getAllDice();
    public void removeDice(Integer id);
}
