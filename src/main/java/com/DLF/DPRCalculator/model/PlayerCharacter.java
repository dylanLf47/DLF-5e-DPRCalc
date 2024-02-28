package com.DLF.DPRCalculator.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String name;
    private String description;
    private int crit_range;
    private double attack_mod;
    private int num_of_attacks;
    private boolean has_adv;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dice> standard_die_list;
    private int standard_dam_mod;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dice> first_hit_die_list;
    private int first_hit_dam_mod;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dice> crit_die_list;
    private int crit_dam_mod;
    private boolean gwm_check;
    private boolean lucky_check;
    private boolean elven_acc_check;
    private boolean crusher_check;
    private boolean piercer_check;
    private int piercer_die;
    private int piercer_reroll;
    @Transient
    private int piercer_param;
    @Transient
    private boolean piercer_param_gwf;
    private boolean stalkers_flurry_check;
    private boolean graze_check;
    private int graze_mod;
    private boolean vex_check;
    @Transient
    private int advantage_value;

    private boolean has_bonus_action;
    private double attack_mod_bonus;
    private int num_of_attacks_bonus;
    private boolean has_adv_bonus;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dice> standard_die_list_bonus;
    private int standard_dam_mod_bonus;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dice> crit_die_list_bonus;
    private int crit_dam_mod_bonus;
    private boolean crusher_check_bonus;
    private boolean piercer_check_bonus;
    private int piercer_die_bonus;
    private int piercer_reroll_bonus;
    @Transient
    private int piercer_param_bonus;
    @Transient
    private boolean piercer_param_gwf_bonus;
    private boolean stalkers_flurry_check_bonus;
    private boolean graze_check_bonus;
    private int graze_mod_bonus;
    private boolean vex_check_bonus;
    @Transient
    private int advantage_value_bonus;

    public PlayerCharacter() {

    }

    public PlayerCharacter(String name, String description, int crit_range, double attack_mod, int num_of_attacks, boolean has_adv, List<Dice> standard_die_list, int standard_dam_mod,
                           List<Dice> first_hit_die_list, int first_hit_dam_mod, List<Dice> crit_die_list, int crit_dam_mod, boolean gwm_check,
                           boolean lucky_check, boolean elven_acc_check, boolean crusher_check, boolean piercer_check, int piercer_die, int piercer_reroll,
                           boolean stalkers_flurry_check, boolean graze_check, int graze_mod, boolean vex_check,
                           boolean has_bonus_action, double attack_mod_bonus, int num_of_attacks_bonus, boolean has_adv_bonus, List<Dice> standard_die_list_bonus, int standard_dam_mod_bonus,
                           List<Dice> crit_die_list_bonus, int crit_dam_mod_bonus, boolean crusher_check_bonus, boolean piercer_check_bonus,
                           int piercer_die_bonus, int piercer_reroll_bonus, boolean stalkers_flurry_check_bonus, boolean graze_check_bonus,
                           int graze_mod_bonus, boolean vex_check_bonus) {
        this.name = name;
        this.description = description;
        this.crit_range = crit_range;
        this.attack_mod = attack_mod;
        this.num_of_attacks = num_of_attacks;
        this.has_adv = has_adv;
        this.standard_die_list = standard_die_list;
        this.standard_dam_mod = standard_dam_mod;
        this.first_hit_die_list = first_hit_die_list;
        this.first_hit_dam_mod = first_hit_dam_mod;
        this.crit_die_list = crit_die_list;
        this.crit_dam_mod = crit_dam_mod;
        this.gwm_check = gwm_check;
        this.lucky_check = lucky_check;
        this.elven_acc_check = elven_acc_check;
        this.crusher_check = crusher_check;
        this.piercer_check = piercer_check;
        this.piercer_die = piercer_die;
        this.piercer_reroll = piercer_reroll;
        if (piercer_check) {
            if (standard_die_list.stream().anyMatch(r -> r.getDie_num() == piercer_die) ||
                    first_hit_die_list.stream().anyMatch(r -> r.getDie_num() == piercer_die)) {
                this.piercer_param = 1;
            }
            else if (crit_die_list.stream().anyMatch(r -> r.getDie_num() == piercer_die)) {
                this.piercer_param = 2;
            }
            this.piercer_param_gwf = standard_die_list.stream().anyMatch(Dice::isIs_gwf) ||
                    first_hit_die_list.stream().anyMatch(Dice::isIs_gwf) ||
                    crit_die_list.stream().anyMatch(Dice::isIs_gwf);

        }
        this.stalkers_flurry_check = stalkers_flurry_check;
        this.graze_check = graze_check;
        this.graze_mod = graze_mod;
        this.vex_check = vex_check;
        this.advantage_value = 1;
        this.advantage_value = this.isHas_adv() ? (this.isElven_acc_check() ? 3 : 2) : 1;

        this.has_bonus_action = has_bonus_action;
        this.attack_mod_bonus = attack_mod_bonus;
        this.num_of_attacks_bonus = num_of_attacks_bonus;
        this.has_adv_bonus = has_adv_bonus;
        this.standard_die_list_bonus = standard_die_list_bonus;
        this.standard_dam_mod_bonus = standard_dam_mod_bonus;
        this.crit_die_list_bonus = crit_die_list_bonus;
        this.crit_dam_mod_bonus = crit_dam_mod_bonus;
        this.crusher_check_bonus = crusher_check_bonus;
        this.piercer_check_bonus = piercer_check_bonus;
        this.piercer_die_bonus = piercer_die_bonus;
        this.piercer_reroll_bonus = piercer_reroll_bonus;
        if (piercer_check_bonus) {

            if (standard_die_list_bonus.stream().anyMatch(r -> r.getDie_num() == piercer_die_bonus) ||
                    first_hit_die_list.stream().anyMatch(r -> r.getDie_num() == piercer_die_bonus)) {
                this.piercer_param_bonus = 1;
            }
            else if (crit_die_list_bonus.stream().anyMatch(r -> r.getDie_num() == piercer_die_bonus)) {
                this.piercer_param_bonus = 2;
            }
            this.piercer_param_gwf_bonus = standard_die_list_bonus.stream().anyMatch(Dice::isIs_gwf) ||
                    first_hit_die_list.stream().anyMatch(Dice::isIs_gwf) ||
                    crit_die_list_bonus.stream().anyMatch(Dice::isIs_gwf);
        }
        this.stalkers_flurry_check_bonus = stalkers_flurry_check_bonus;
        this.graze_check_bonus = graze_check_bonus;
        this.graze_mod_bonus = graze_mod_bonus;
        this.vex_check_bonus = vex_check_bonus;
        this.advantage_value_bonus = this.isHas_adv_bonus() ? (this.isElven_acc_check() ? 3 : 2) : 1;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getAttack_mod() {
        return attack_mod;
    }

    public int getCrit_range() {
        return crit_range;
    }

    public int getNum_of_attacks() {
        return num_of_attacks;
    }

    public boolean isHas_adv() {
        return has_adv;
    }

    @JsonManagedReference
    public List<Dice> getStandard_die_list() {
        return standard_die_list;
    }

    public void setStandard_die_list(List<Dice> standard_die_list) {
        this.standard_die_list = standard_die_list;
    }

    public int getStandard_dam_mod() {
        return standard_dam_mod;
    }

    @JsonManagedReference
    public List<Dice> getFirst_hit_die_list() {
        return first_hit_die_list;
    }

    public void setFirst_hit_die_list(List<Dice> first_hit_die_list) {
        this.first_hit_die_list = first_hit_die_list;
    }

    public int getFirst_hit_dam_mod() {
        return first_hit_dam_mod;
    }

    @JsonManagedReference
    public List<Dice> getCrit_die_list() {
        return crit_die_list;
    }

    public void setCrit_die_list(List<Dice> crit_die_list) {
        this.crit_die_list = crit_die_list;
    }

    public int getCrit_dam_mod() {
        return crit_dam_mod;
    }

    public boolean isGwm_check() {
        return gwm_check;
    }

    public boolean isLucky_check() {
        return lucky_check;
    }

    public boolean isElven_acc_check() {
        return elven_acc_check;
    }

    public boolean isCrusher_check() {
        return crusher_check;
    }

    public boolean isPiercer_check() {
        return piercer_check;
    }

    public int getPiercer_die() {
        return piercer_die;
    }

    public int getPiercer_reroll() {
        return piercer_reroll;
    }

    public int getPiercer_param() {
        return piercer_param;
    }

    public boolean isPiercer_param_gwf() {
        return piercer_param_gwf;
    }

    public boolean isStalkers_flurry_check() {
        return stalkers_flurry_check;
    }

    public boolean isGraze_check() {
        return graze_check;
    }

    public int getGraze_mod() {
        return graze_mod;
    }

    public boolean isVex_check() {
        return vex_check;
    }

    public int getAdvantage_value() {
        return advantage_value;
    }

    public boolean isHas_bonus_action() {
        return has_bonus_action;
    }

    public double getAttack_mod_bonus() {
        return attack_mod_bonus;
    }

    public int getNum_of_attacks_bonus() {
        return num_of_attacks_bonus;
    }

    public boolean isHas_adv_bonus() {
        return has_adv_bonus;
    }

    @JsonManagedReference
    public List<Dice> getStandard_die_list_bonus() {
        return standard_die_list_bonus;
    }

    public void setStandard_die_list_bonus(List<Dice> standard_die_list_bonus) {
        this.standard_die_list_bonus = standard_die_list_bonus;
    }

    public int getStandard_dam_mod_bonus() {
        return standard_dam_mod_bonus;
    }

    @JsonManagedReference
    public List<Dice> getCrit_die_list_bonus() {
        return crit_die_list_bonus;
    }

    public void setCrit_die_list_bonus(List<Dice> crit_die_list_bonus) {
        this.crit_die_list_bonus = crit_die_list_bonus;
    }

    public int getCrit_dam_mod_bonus() {
        return crit_dam_mod_bonus;
    }

    public boolean isCrusher_check_bonus() {
        return crusher_check_bonus;
    }

    public boolean isPiercer_check_bonus() {
        return piercer_check_bonus;
    }

    public int getPiercer_die_bonus() {
        return piercer_die_bonus;
    }

    public int getPiercer_reroll_bonus() {
        return piercer_reroll_bonus;
    }

    public int getPiercer_param_bonus() {
        return piercer_param_bonus;
    }

    public boolean isPiercer_param_gwf_bonus() {
        return piercer_param_gwf_bonus;
    }

    public boolean isStalkers_flurry_check_bonus() {
        return stalkers_flurry_check_bonus;
    }

    public boolean isGraze_check_bonus() {
        return graze_check_bonus;
    }

    public int getGraze_mod_bonus() {
        return graze_mod_bonus;
    }

    public boolean isVex_check_bonus() {
        return vex_check_bonus;
    }

    public int getAdvantage_value_bonus() {
        return advantage_value_bonus;
    }
}
