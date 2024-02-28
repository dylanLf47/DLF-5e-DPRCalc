package com.DLF.DPRCalculator.controller;

import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;
import com.DLF.DPRCalculator.service.DiceService;
import com.DLF.DPRCalculator.service.PlayerCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dice")
@CrossOrigin(origins = "https://dlf-5e-dpr-calculator.onrender.com")
public class DiceController {

    @Autowired
    private DiceService diceService;

    @PostMapping("/add")
    public void add(@RequestBody Dice dice) {
        diceService.saveDice(dice);
    }

    @GetMapping("/getAll")
    public List<Dice> list(){
        return diceService.getAllDice();
    }

    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable("id") Integer id) {
        diceService.removeDice(id);
    }
}
