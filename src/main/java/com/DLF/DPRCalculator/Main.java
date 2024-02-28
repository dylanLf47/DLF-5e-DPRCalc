package com.DLF.DPRCalculator;

import com.DLF.DPRCalculator.controller.Calculator;
import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Dice> standard = new ArrayList<>();
        List<Dice> firstHit = new ArrayList<>();
        List<Dice> crit = new ArrayList<>();
        List<Dice> standardB = new ArrayList<>();
        List<Dice> critB = new ArrayList<>();
        standard.add(new Dice(2,6,true,false,false,"standard",null));
        PlayerCharacter pc = new PlayerCharacter("Dylan", "Dueling", 20, 5, 2, false, standard, 3, firstHit, 0, crit, 0, false, false, false, false, false,
                4,0, false, false,0, false, false,0,0,
                false,standardB,0,critB,0, false, false,0,0, false, false,0, false);
        String dpr = new Calculator(pc, 13).getResult();
        System.out.println(dpr);
    }
}
