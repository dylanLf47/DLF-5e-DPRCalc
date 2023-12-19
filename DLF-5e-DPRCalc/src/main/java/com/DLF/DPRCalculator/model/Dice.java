package com.DLF.DPRCalculator.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Dice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dice_id;
    private int dice_amount;
    private int die_num;
    private boolean is_gwf;
    private boolean is_ea;
    private boolean is_sa;
    private String list;
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerCharacter player_character;

    public Dice(int dice_amount, int die_num, boolean is_gwf, boolean is_ea, boolean is_sa, String list, PlayerCharacter playerCharacter) {
        this.dice_amount = dice_amount;
        this.die_num = die_num;
        this.is_gwf = is_gwf;
        this.is_ea = is_ea;
        this.is_sa = is_sa;
        this.list = list;
        this.player_character = playerCharacter;
    }

    public Dice() {

    }

    public int getDiceId() {
        return dice_id;
    }

    public int getDice_amount() {
        return dice_amount;
    }

    public int getDie_num() {
        return die_num;
    }

    public boolean isIs_gwf() {
        return is_gwf;
    }

    public boolean isIs_ea() {
        return is_ea;
    }

    public boolean isIs_sa() {
        return is_sa;
    }

    public String getList() {
        return list;
    }

    @JsonBackReference
    public PlayerCharacter getPlayer_character() {
        return player_character;
    }

    public void setPlayer_character(PlayerCharacter playerCharacter) {
        this.player_character = playerCharacter;
    }
//(double)(die_num + 1)/2
    public double totalDieAvg(int multiplier) {
        if (is_gwf && is_ea) {
            int dieSum = 0;
            for (int i = 0; i <= die_num; i++) {
                dieSum += i;
            }
            return (multiplier * dice_amount) * ((dieSum + ((1/(double)die_num) + ((double)(die_num + 1)/2)) * 2)/(double)die_num);
        }
        else if (is_gwf) return (multiplier * dice_amount) * ((((double)(die_num + 1)/2) * 2 - 3) / (double)die_num + ((double)(die_num + 1)/2));
        else if (is_ea) return (multiplier * dice_amount) * ((1/(double)die_num) + ((double)(die_num + 1)/2));
        else return (multiplier * dice_amount) * ((double)(die_num + 1)/2);
    }

    public double savageAttackerAvg(int multiplier) {
        double savAtSum = totalDieAvg(1);
        double tempSavAtSum = 0;
        for (int i = 2; i < die_num; i++) {
            if (is_gwf) {
                tempSavAtSum += (totalDieAvg(1) * 2) + (i * (i - 2));
            }
            else tempSavAtSum += i*i;
            for (int j = i + 1; j <= die_num; j++) {
                tempSavAtSum += j;
            }
            tempSavAtSum /= die_num;
            savAtSum += tempSavAtSum;
            tempSavAtSum = 0;
        }
        savAtSum += die_num;
        savAtSum /= die_num;
        savAtSum -= ((double)(die_num + 1)/2);
        return multiplier * savAtSum;
    }
}
