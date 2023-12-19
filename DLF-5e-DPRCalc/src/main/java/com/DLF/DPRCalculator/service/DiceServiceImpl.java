package com.DLF.DPRCalculator.service;

import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;
import com.DLF.DPRCalculator.repository.DiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiceServiceImpl implements DiceService{
    @Autowired
    private DiceRepository diceRepository;

    @Override
    public Dice saveDice(Dice dice) {
        return diceRepository.save(dice);
    }

    @Override
    public List<Dice> getAllDice() {
        return diceRepository.findAll();
    }

    @Override
    public void removeDice(Integer id) {
        diceRepository.deleteById(id);
    }
}
