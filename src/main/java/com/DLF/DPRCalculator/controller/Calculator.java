package com.DLF.DPRCalculator.controller;

import com.DLF.DPRCalculator.controller.Tree.*;
import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;

import java.text.DecimalFormat;
import java.util.*;

public class Calculator {
    private PlayerCharacter pc;
    private int ac;
    private List<Double> chancesOfMiss = new ArrayList<>();
    private Map<Double, Double[]> firstHitDamageMap = new HashMap<>();
    private Double hashCounterFirstHit = 0.0;
    private double flurryChanceOfMiss = 1.0;
    private double flurryAttack = 0.0;
    private double chanceOfMissPiercer = 1.0;
    private double piercerDamage = 0;

    private Map<Double, Double[]> saDamageMap = new HashMap<>();
    private double hashCounterSa = 0.0;

    private BinaryTree damageTree = new BinaryTree();
    private Node root;
    private int damageTreeTracker = 0;

    /**
     * Constructor for the Calulator
     * @param pc Player Character that will have their damage calculated
     * @param ac Armor Class that the Player Character is going to calculate against
     */
    public Calculator(PlayerCharacter pc, int ac) {
        this.pc = pc;
        this.ac = ac;
    }

    /**
     * Calculates the PC's DPR against the AC, and returns the DPR value
     * @return DPR value of the PC against the AC
     */
    public String getResult() {
        double dpr = calculateDprAgainstAC(pc, ac);
        DecimalFormat df = new DecimalFormat("#.####");

        return "" + df.format(dpr);
    }

    /**
     * Uses all the information (Attacks and Bonus Action Attacks) of the character to
     * calculate DPR against the current AC
     * @param pc the character currently being calculated
     * @param theAc the AC currently being calculated against
     * @return current character's total DPR against the current AC
     */
    public double calculateDprAgainstAC(PlayerCharacter pc, int theAc) {
        double totalDpr = 0;

        double damagePerStandardHit = pc.getStandard_die_list().stream().mapToDouble(r -> r.totalDieAvg(1)).sum() + pc.getStandard_dam_mod();
        double damagePerStandardHitCrit = pc.getStandard_die_list().stream().mapToDouble(r -> r.totalDieAvg(2)).sum() + pc.getCrit_die_list().stream().mapToDouble(r -> r.totalDieAvg(1)).sum() + pc.getStandard_dam_mod() + pc.getCrit_dam_mod();
        double damagePerFirstHit = pc.getFirst_hit_die_list().stream().mapToDouble(r -> r.totalDieAvg(1)).sum() + pc.getFirst_hit_dam_mod();
        double damagePerFirstHitCrit = pc.getFirst_hit_die_list().stream().mapToDouble(r -> r.totalDieAvg(2)).sum() + pc.getFirst_hit_dam_mod();
        double saDamage = pc.getStandard_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum() +
                pc.getFirst_hit_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum();
        double saDamageCrit = pc.getStandard_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(2)).sum() +
                pc.getFirst_hit_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(2)).sum() +
                pc.getCrit_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum();

        double damagePerStandardHitBonus = pc.getStandard_die_list_bonus().stream().mapToDouble(r -> r.totalDieAvg(1)).sum() + pc.getStandard_dam_mod_bonus();
        double damagePerStandardHitCritBonus = pc.getStandard_die_list_bonus().stream().mapToDouble(r -> r.totalDieAvg(2)).sum() + pc.getCrit_die_list_bonus().stream().mapToDouble(r -> r.totalDieAvg(1)).sum() + pc.getStandard_dam_mod_bonus() + pc.getCrit_dam_mod_bonus();
        double saDamageBonus = pc.getStandard_die_list_bonus().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum() +
                pc.getFirst_hit_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum();
        double saDamageCritBonus = pc.getStandard_die_list_bonus().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(2)).sum() +
                pc.getFirst_hit_die_list().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(2)).sum() +
                pc.getCrit_die_list_bonus().stream().filter(Dice::isIs_sa).mapToDouble(r -> r.savageAttackerAvg(1)).sum();

        totalDpr += calculateDamageAgainstAC(pc.isHas_adv(), pc.isElven_acc_check(), pc.getCrit_range(), theAc, pc.getAttack_mod(), damagePerStandardHit,
                pc.getStandard_dam_mod(), damagePerStandardHitCrit, pc.isGraze_check(), pc.getGraze_mod(), pc.isVex_check(), pc.isCrusher_check(),
                pc.getNum_of_attacks(), damagePerFirstHit, damagePerFirstHitCrit, pc.getAdvantage_value(), pc.isLucky_check(), false, saDamage, saDamageCrit,
                pc.isPiercer_check(), pc.getPiercer_die(), pc.getPiercer_reroll(), pc.getPiercer_param(), pc.isPiercer_param_gwf(), pc.isStalkers_flurry_check());

        if (pc.isGwm_check()) {
            double chanceOfCrit = calculateCritPercent(pc.getCrit_range(), pc.getAdvantage_value(), pc.isLucky_check());
            chanceOfCrit = 1 - Math.pow(1 - chanceOfCrit, pc.getNum_of_attacks());
            int adv2 = pc.isElven_acc_check() ? 3 : 2;
            if (pc.isVex_check() && !pc.isHas_adv()) {
                double critPercent = calculateCritPercent(pc.getCrit_range(), 1, pc.isLucky_check());
                double critPercentAdv = calculateCritPercent(pc.getCrit_range(), adv2, pc.isLucky_check());
                double missPercent = 1 - calculateChanceOfHit(theAc, pc.getAttack_mod(), 1, pc.isLucky_check());
                double missPercentAdv = 1 - calculateChanceOfHit(theAc, pc.getAttack_mod(), adv2, pc.isLucky_check());
                double normalHitPercent = calculateChanceOfHit(theAc, pc.getAttack_mod(), 1, pc.isLucky_check()) - critPercent;
                double normalHitPercentAdv = calculateChanceOfHit(theAc, pc.getAttack_mod(), adv2, pc.isLucky_check()) - critPercentAdv;


                chanceOfCrit = buildTree(critPercent, critPercentAdv, missPercent, pc.getNum_of_attacks(), normalHitPercent, normalHitPercentAdv, missPercentAdv, false);
            }
            if (pc.isGwm_check() && !pc.isHas_bonus_action()) {
                totalDpr += chanceOfCrit * calculateDamageAgainstAC(pc.isHas_adv(), pc.isElven_acc_check(), pc.getCrit_range(), theAc, pc.getAttack_mod(), damagePerStandardHit,
                        pc.getStandard_dam_mod(), damagePerStandardHitCrit, pc.isGraze_check(), pc.getGraze_mod(), pc.isVex_check(), pc.isCrusher_check(),
                        1, damagePerFirstHit, damagePerFirstHitCrit, pc.isVex_check() ? adv2 : pc.getAdvantage_value(), pc.isLucky_check(), true, 0, 0,
                        pc.isPiercer_check(), pc.getPiercer_die(), pc.getPiercer_reroll(), pc.getPiercer_param(), pc.isPiercer_param_gwf(), pc.isStalkers_flurry_check());
            } else if (pc.isGwm_check() && pc.isHas_bonus_action()) {
                totalDpr += chanceOfCrit * calculateDamageAgainstAC(pc.isHas_adv(), pc.isElven_acc_check(), pc.getCrit_range(), theAc, pc.getAttack_mod(), damagePerStandardHit,
                        pc.getStandard_dam_mod(), damagePerStandardHitCrit, pc.isGraze_check(), pc.getGraze_mod(), pc.isVex_check(), pc.isCrusher_check(),
                        1, damagePerFirstHit, damagePerFirstHitCrit, pc.isVex_check() ? adv2 : pc.getAdvantage_value(), pc.isLucky_check(), true, 0, 0,
                        pc.isPiercer_check(), pc.getPiercer_die(), pc.getPiercer_reroll(), pc.getPiercer_param(), pc.isPiercer_param_gwf(), pc.isStalkers_flurry_check())
                        + (1 - chanceOfCrit) * calculateDamageAgainstAC(pc.isHas_adv_bonus(), pc.isElven_acc_check(), pc.getCrit_range(), theAc, pc.getAttack_mod_bonus(), damagePerStandardHitBonus,
                        pc.getStandard_dam_mod_bonus(), damagePerStandardHitCritBonus, pc.isGraze_check_bonus(), pc.getGraze_mod_bonus(), pc.isVex_check_bonus(), false,
                        pc.getNum_of_attacks_bonus(), damagePerFirstHit, damagePerFirstHitCrit, pc.getAdvantage_value_bonus(), pc.isLucky_check(), false, saDamageBonus, saDamageCritBonus,
                        pc.isPiercer_check_bonus(), pc.getPiercer_die_bonus(), pc.getPiercer_reroll(), pc.getPiercer_param_bonus(), pc.isPiercer_param_gwf_bonus(), pc.isStalkers_flurry_check_bonus());
            }
        }

        else if (pc.isHas_bonus_action()) {
            totalDpr += calculateDamageAgainstAC(pc.isHas_adv(), pc.isElven_acc_check(), pc.getCrit_range(), theAc, pc.getAttack_mod_bonus(), damagePerStandardHitBonus,
                    pc.getStandard_dam_mod_bonus(), damagePerStandardHitCritBonus, pc.isGraze_check_bonus(), pc.getGraze_mod_bonus(), pc.isVex_check_bonus(), false,
                    pc.getNum_of_attacks_bonus(), damagePerFirstHit, damagePerFirstHitCrit, pc.getAdvantage_value_bonus(), pc.isLucky_check(), false, saDamageBonus, saDamageCritBonus,
                    pc.isPiercer_check_bonus(), pc.getPiercer_die_bonus(), pc.getPiercer_reroll(), pc.getPiercer_param_bonus(), pc.isPiercer_param_gwf_bonus(), pc.isStalkers_flurry_check_bonus());
        }

        totalDpr += calculateFirstHitDamage(firstHitDamageMap);
        firstHitDamageMap.clear();

        totalDpr += calculateSaDamage(saDamageMap);
        saDamageMap.clear();

        totalDpr += flurryAttack;
        flurryAttack = 0;
        flurryChanceOfMiss = 0;

        damageTree = new BinaryTree();
        damageTreeTracker = 0;

        return totalDpr;
    }

    /**
     * Calculates current attack (Either Attack or Bonus Action Attack) against current AC
     * @param hasAdv does this attack have advantage?
     * @param hasElAc does this character have elven accuracy
     * @param critRange character's critical hit range
     * @param theAc current AC
     * @param aMod attacks' attack modifier
     * @param damagePerStandardHit attack's damage dealt on standard hit
     * @param standardMod attack's damage modifier of a standard hit
     * @param damagePerStandardHitCrit attack's damage dealt on critical hit
     * @param hasGraze does this attack have graze?
     * @param grazeMod modifier used for graze
     * @param hasVex does this attack have vex?
     * @param hasCrusher does this attack have crusher?
     * @param attacks amount of attacks for attack
     * @param damagePerFirstHit attack's damage dealt on first hit
     * @param damagePerFirstHitCrit attack's critical hit damage dealt on first hit
     * @param advantageValue rerolls with or without advantage of attack
     * @param hasLucky does the character have lucky?
     * @param isGwm does the character have GWM?
     * @param saDamage damage of savage attacker
     * @param saDamageCrit damage of savage attacker critical hit
     * @param hasPiercer does this attack have piercer?
     * @param piercerDie die to reroll with piercer
     * @param piercerRerollNum reroll die when rolled at or below value
     * @param piercerParam piercer reroll when standard hit or critical hit
     * @param piercerParamGwf does piercer die benefit from GWF?
     * @param hasFlurry does this attack have stalker's flurry
     * @return total damage of the current attack
     */
    public double calculateDamageAgainstAC(boolean hasAdv, boolean hasElAc, int critRange, int theAc, double aMod, double damagePerStandardHit, double standardMod, double damagePerStandardHitCrit,
                                           boolean hasGraze, int grazeMod, boolean hasVex, boolean hasCrusher, int attacks, double damagePerFirstHit, double damagePerFirstHitCrit,
                                           int advantageValue, boolean hasLucky, boolean isGwm, double saDamage, double saDamageCrit, boolean hasPiercer, int piercerDie, int piercerRerollNum, int piercerParam, boolean piercerParamGwf, boolean hasFlurry) {
        double totalDamage = 0;
        double chanceOfHit = calculateChanceOfHit(theAc, aMod, advantageValue, hasLucky);
        double critHitPercent = Math.max(0, calculateCritPercent(critRange, advantageValue, hasLucky));
        double normalHitPercent = chanceOfHit - critHitPercent;
        double missPercent = 1 - normalHitPercent - critHitPercent;

        if (!isGwm) {
            if (damagePerFirstHit > 0) {
                double chanceOfMiss = Math.pow(missPercent, attacks);
                double firstHitDamage = (1 - chanceOfMiss) * damagePerFirstHit * (normalHitPercent / (normalHitPercent + critHitPercent)) + (1 - chanceOfMiss) * damagePerFirstHitCrit * (critHitPercent / (normalHitPercent + critHitPercent));
                Double[] missHitArray = {chanceOfMiss, firstHitDamage};
                firstHitDamageMap.put(hashCounterFirstHit++, missHitArray);
            }
        }
        if (saDamage > 0) {
            double chanceOfMiss = Math.pow(missPercent, attacks);
            double saExtraDamage = (1 - chanceOfMiss) * saDamage * (normalHitPercent / (normalHitPercent + critHitPercent)) + (1 - chanceOfMiss) * saDamageCrit * (critHitPercent / (normalHitPercent + critHitPercent));
            Double[] missHitArray = {chanceOfMiss, saExtraDamage};
            saDamageMap.put(hashCounterSa++, missHitArray);
        }
        if (hasPiercer) {
            if (piercerParam == 1) {
                totalDamage += chanceOfMissPiercer * (1-Math.pow(missPercent, attacks)) * calculatePiercer(piercerDie, piercerRerollNum, piercerParamGwf);
                chanceOfMissPiercer *= chanceOfMissPiercer * Math.pow(missPercent, attacks);
            }
            else if (piercerParam == 2) {
                totalDamage += chanceOfMissPiercer * (1-Math.pow(1 - critHitPercent, attacks)) * calculatePiercer(piercerDie, piercerRerollNum, piercerParamGwf);
                chanceOfMissPiercer *= chanceOfMissPiercer * Math.pow(1 - critHitPercent, attacks);
            }
        }

        double standardHit = (normalHitPercent * (damagePerStandardHit) + (critHitPercent * (damagePerStandardHitCrit)));

        if (hasFlurry) {
            flurryChanceOfMiss *= 1 - Math.pow(chanceOfHit, attacks);
            flurryAttack += standardHit * flurryChanceOfMiss;
        }
        if (hasGraze) {
            standardHit += (missPercent * grazeMod);
        }

        if (hasAdv || !(hasVex || hasCrusher)) {
            totalDamage += (standardHit * attacks);
        } else {
            int adv2 = hasElAc ? 3 : 2;
            double chanceOfHitAdv = calculateChanceOfHit(theAc, aMod, adv2, hasLucky);
            double critHitPercentAdv = Math.max(0, calculateCritPercent(critRange, adv2, hasLucky));
            double normalHitPercentAdv = chanceOfHitAdv - critHitPercentAdv;
            double missPercentAdv = 1 - normalHitPercentAdv - critHitPercentAdv;
            double standardHitAdv = (normalHitPercentAdv * (damagePerStandardHit) + (critHitPercentAdv * (damagePerStandardHitCrit)));
            if (hasGraze) {
                standardHitAdv += (missPercentAdv * grazeMod);
            }
            if (hasCrusher && !hasVex) {
                totalDamage += standardHit;
                double currentPercent = 1.0;
                for (int i = 1; i <= attacks - 1; i ++) {
                    totalDamage += (1 - critHitPercent) * currentPercent * standardHit + (critHitPercent * currentPercent * standardHitAdv * Math.abs(i - attacks));
                    currentPercent *= (1 - critHitPercent);
                }
            }
            else if (hasVex && !hasCrusher) {
                totalDamage += buildTree(standardHit, standardHitAdv,
                        missPercent, attacks, chanceOfHit, chanceOfHitAdv, missPercentAdv, false);
            }
            else if (hasVex && hasCrusher) {
                totalDamage += buildTree(standardHit, standardHitAdv,
                        missPercent, attacks, chanceOfHit - critHitPercent, chanceOfHitAdv - critHitPercentAdv, missPercentAdv, true);
                totalDamage += damageTree.getCrusherList().stream().mapToDouble(r -> r).sum();
            }
        }
        return totalDamage;
    }

    /**
     * Calculates chance of hitting of current attack
     * @param theAc current AC attacking against
     * @param aMod attack modifier
     * @param advantageValue amount of rolls with or without advantage
     * @param hasLucky does the character have the Halfling lucky feature?
     * @return chance of hit
     */
    public double calculateChanceOfHit(int theAc, double aMod, int advantageValue, boolean hasLucky) {
        double chanceOfHit = 1 - Math.pow(1 - (21 - theAc + aMod) / 20.0, advantageValue);
        if (hasLucky) chanceOfHit += calculateLuckyIncrease(1 - Math.pow(1 - (21 - theAc + aMod) / 20.0, 1), 1 - Math.pow(1 - (21 - theAc + aMod) / 20.0, 1),
                1 - Math.pow(1 - (21 - theAc + aMod) / 20.0, 1), advantageValue, 0);
        return chanceOfHit;
    }

    /**
     * Calculates chance of a critical hit
     * @param critRange critical hit range of character
     * @param advantageValue amount of rolls with or without advantage
     * @param hasLucky does the character have the Halfling lucky feature?
     * @return chance of critical hit
     */
    public double calculateCritPercent(int critRange, int advantageValue, boolean hasLucky) {
        double crit = 1 - Math.pow((1 - ((critRange < 21 ? 21 - critRange : 0) / 20.0)), advantageValue);
        if (hasLucky) crit += calculateLuckyIncrease(calculateCritPercent(critRange, 1, false),
                calculateCritPercent(critRange, 1, false),
                calculateCritPercent(critRange, 1, false), advantageValue, 0);
        return crit;
    }

    /**
     * Calculates damage of first hit
     * @param theFirstHitDamageMap map containing chances of triggering first hits and damage of first hit
     * @return damage of first hit
     */
    public double calculateFirstHitDamage(Map<Double, Double[]> theFirstHitDamageMap) {
        Iterator<Double> firstHitIt = theFirstHitDamageMap.keySet().iterator();
        double firstHitDamage = 0;
        double currentPercent = 1;
        while (firstHitIt.hasNext()) {
            Double currentKey = firstHitIt.next();
            Double currentEntry1 = theFirstHitDamageMap.get(currentKey)[0];
            Double currentEntry2 = theFirstHitDamageMap.get(currentKey)[1];
            firstHitDamage += currentPercent * currentEntry2;
            currentPercent = currentEntry1 * currentPercent;
        }
        return firstHitDamage;
    }
    /**
     * Calculates damage of the savage attacker feat
     * @param theSaDamageMap map containing chances of triggering savage attacker and its damage
     * @return damage of savage attacker feat
     */
    public double calculateSaDamage(Map<Double, Double[]> theSaDamageMap) {
        Iterator<Double> saIt = theSaDamageMap.keySet().iterator();
        double saDamage = 0;
        double currentPercent = 1;
        while (saIt.hasNext()) {
            Double currentKey = saIt.next();
            Double currentEntry1 = saDamageMap.get(currentKey)[0];
            Double currentEntry2 = saDamageMap.get(currentKey)[1];
            saDamage += currentPercent * currentEntry2;
            currentPercent = currentEntry1 * currentPercent;
        }
        return saDamage;
    }

    /**
     * Calculates damage of the piercer feat
     * @param piercerDie Die to be rerolled once per turn
     * @param piercerReroll Reroll die when it rolls at or below value
     * @param paramGwf Does the die benefit from GWM?
     * @return Damage of piercer
     */
    public double calculatePiercer(int piercerDie, int piercerReroll, boolean paramGwf) {
        int tempSum = 0;
        for (int i = 1; i <= piercerReroll; i++) {
            tempSum += i;
        }
        return ((new Dice(1, piercerDie, paramGwf, false, false, "", pc).totalDieAvg(1) * piercerReroll) - tempSum)/piercerDie;
    }

    /**
     * Calculates increased chances when benefiting from the Halfling lucky feature
     * @param chanceAdv1 Chance to hit when rerolling once
     * @param chanceAdv2 Chance to hit when rerolling twice
     * @param chanceAdv3 Chance to hit when rerolling thrice
     * @param advantageValue amount of rolls with or without advantage
     * @param minusValue Minus from percentage
     * @return Increase to chances with Lucky
     */
    public double calculateLuckyIncrease(double chanceAdv1, double chanceAdv2, double chanceAdv3, int advantageValue, double minusValue) {
        double increase = 0;
        //double tempHitChance = chanceAdv1;
        if (advantageValue == 1) {
            increase += 0.05 * chanceAdv1;
        }
        else if (advantageValue == 2) {
            increase += (0.05 * 0.05) * chanceAdv2 +
                    2*((1 - chanceAdv1 - minusValue - 0.05) * 0.05) * chanceAdv1;
        }
        else if (advantageValue == 3) {
            increase += (0.05 * 0.05 * 0.05) * chanceAdv3 +
                    3 * 0.05 * (1 - chanceAdv1 - minusValue - 0.05) * 0.05 * chanceAdv2 +
                    3 * (0.05 * (1 - chanceAdv1 - minusValue - 0.05) * (1 - chanceAdv1 - minusValue - 0.05)) * chanceAdv1;
        }
        return increase;
    }

    /**
     * Builds tree that calculates chances and damage of every possible roll when
     * benefiting from the Vex feature
     * @param value damage without advantage
     * @param valueAdv damage with advantage
     * @param missPercent chances of missing
     * @param attacks amount of attacks
     * @param chanceOfHit chance to hit
     * @param chanceOfHitAdv chance to hit with advantage
     * @param missPercentAdv chance of missing with advantage
     * @param crusher does this attack have crusher?
     * @return damage of using vex
     */
    public double buildTree(double value, double valueAdv, double missPercent,
                            int attacks, double chanceOfHit, double chanceOfHitAdv, double missPercentAdv, boolean crusher) {
        //int adv2 = hasElAc ? 3 : 2;
        double total = 0.0;

        if (damageTree.root() == null) {
            root = new Node(1, value, null);
        }
        for (int i = 1; i <= attacks; i++) {
            for (int j = 1; j <= Math.pow(2, damageTreeTracker); j++) {
                total += damageTree.insert(root, missPercent, missPercentAdv, chanceOfHit, chanceOfHitAdv, value, valueAdv, crusher);
            }
            damageTreeTracker++;
            if (crusher && !damageTree.getCrusherList().isEmpty() && attacks > 1 && i < attacks) {
                List<Double> tempCrusherList = new ArrayList<>(damageTree.getCrusherList());
                List<Double> tempCrusherList2 = new ArrayList<>(tempCrusherList);
                for (Double d : tempCrusherList) {
                    tempCrusherList2.addAll(Collections.nCopies(1, d));
                    damageTree.setCrusherList(tempCrusherList2);
                }
            }
        }
        return total;
    }

    public void buildHeap() {

    }
}
