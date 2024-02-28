package com.DLF.DPRCalculator.service;

import com.DLF.DPRCalculator.controller.Calculator;
import com.DLF.DPRCalculator.model.Dice;
import com.DLF.DPRCalculator.model.PlayerCharacter;
import com.DLF.DPRCalculator.repository.DiceRepository;
import com.DLF.DPRCalculator.repository.PlayerCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerCharacterServiceImpl implements PlayerCharacterService {

    @Autowired
    private PlayerCharacterRepository playerCharacterRepository;
    @Autowired
    private DiceRepository diceRepository;

    @Override
    public PlayerCharacter savePlayerCharacter(PlayerCharacter playerCharacter) {
        PlayerCharacter theCharacter = playerCharacter;
        List<Dice> standardDice = new ArrayList<>();
        for(Dice diceIn : playerCharacter.getStandard_die_list()) {
            Dice tempDice = new Dice(diceIn.getDice_amount(), diceIn.getDie_num(), diceIn.isIs_gwf(), diceIn.isIs_ea(), diceIn.isIs_sa(), diceIn.getList(), playerCharacter);
            tempDice.setPlayer_character(theCharacter);
            standardDice.add(tempDice);
        }
        playerCharacter.setStandard_die_list(standardDice);
        List<Dice> firstHitDice = new ArrayList<>();
        for(Dice diceIn : playerCharacter.getFirst_hit_die_list()) {
            Dice tempDice = new Dice(diceIn.getDice_amount(), diceIn.getDie_num(), diceIn.isIs_gwf(), diceIn.isIs_ea(), diceIn.isIs_sa(), diceIn.getList(), playerCharacter);
            tempDice.setPlayer_character(theCharacter);
            firstHitDice.add(tempDice);
        }
        playerCharacter.setFirst_hit_die_list(firstHitDice);
        List<Dice> critDice = new ArrayList<>();
        for(Dice diceIn : playerCharacter.getCrit_die_list()) {
            Dice tempDice = new Dice(diceIn.getDice_amount(), diceIn.getDie_num(), diceIn.isIs_gwf(), diceIn.isIs_ea(), diceIn.isIs_sa(), diceIn.getList(), playerCharacter);
            tempDice.setPlayer_character(theCharacter);
            critDice.add(tempDice);
        }
        playerCharacter.setCrit_die_list(critDice);
        List<Dice> standardDiceBonus = new ArrayList<>();
        for(Dice diceIn : playerCharacter.getStandard_die_list_bonus()) {
            Dice tempDice = new Dice(diceIn.getDice_amount(), diceIn.getDie_num(), diceIn.isIs_gwf(), diceIn.isIs_ea(), diceIn.isIs_sa(), diceIn.getList(), playerCharacter);
            tempDice.setPlayer_character(theCharacter);
            standardDiceBonus.add(tempDice);
        }
        playerCharacter.setStandard_die_list_bonus(standardDiceBonus);
        List<Dice> critDiceBonus = new ArrayList<>();
        for(Dice diceIn : playerCharacter.getCrit_die_list_bonus()) {
            Dice tempDice = new Dice(diceIn.getDice_amount(), diceIn.getDie_num(), diceIn.isIs_gwf(), diceIn.isIs_ea(), diceIn.isIs_sa(), diceIn.getList(), playerCharacter);
            tempDice.setPlayer_character(theCharacter);
            critDiceBonus.add(tempDice);
        }
        playerCharacter.setCrit_die_list_bonus(critDiceBonus);
        return playerCharacterRepository.save(theCharacter);
    }

    @Override
    public List<PlayerCharacter> getAllPlayerCharacters() {
        return playerCharacterRepository.findAll();
    }

    @Override
    public List<PlayerCharacter> getPlayerCharactersByUsername(String username) {
        return playerCharacterRepository.findByUsername(username);
    }

    @Override
    public void removePlayerCharacter(Integer id) {
        playerCharacterRepository.deleteById(id);
    }

    @Override
    public void removePlayerCharacterByUsername(String username) {
        playerCharacterRepository.removeByUsername(username);
    }

    @Override
    public String calculate(PlayerCharacter pc, int ac){
        PlayerCharacter tempPc = new PlayerCharacter(pc.getName(), pc.getDescription(), pc.getCrit_range(), pc.getAttack_mod(), pc.getNum_of_attacks(),
                pc.isHas_adv(), pc.getStandard_die_list(), pc.getStandard_dam_mod(), pc.getFirst_hit_die_list(), pc.getFirst_hit_dam_mod(),
                pc.getCrit_die_list(), pc.getCrit_dam_mod(), pc.isGwm_check(), pc.isLucky_check(), pc.isElven_acc_check(), pc.isCrusher_check(),
                pc.isPiercer_check(), pc.getPiercer_die(), pc.getPiercer_reroll(), pc.isStalkers_flurry_check(), pc.isGraze_check(), pc.getGraze_mod(), pc.isVex_check(),
                pc.isHas_bonus_action(), pc.getAttack_mod_bonus(), pc.getNum_of_attacks_bonus(), pc.isHas_adv_bonus(), pc.getStandard_die_list_bonus(), pc.getStandard_dam_mod_bonus(),
                pc.getCrit_die_list_bonus(), pc.getCrit_dam_mod_bonus(), pc.isCrusher_check_bonus(), pc.isPiercer_check_bonus(), pc.getPiercer_die_bonus(), pc.getPiercer_reroll_bonus(),
                pc.isStalkers_flurry_check_bonus(), pc.isGraze_check_bonus(), pc.getGraze_mod_bonus(), pc.isVex_check_bonus());
        return new Calculator(tempPc, ac).getResult();
    }

    @Override
    public PlayerCharacter getPlayerCharacter(Integer id) {
        return playerCharacterRepository.findById(id).get();
    }

}
