package com.DLF.DPRCalculator.controller;

import com.DLF.DPRCalculator.model.PlayerCharacter;
import com.DLF.DPRCalculator.service.PlayerCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player_character")
@CrossOrigin
public class PlayerCharacterController {

    @Autowired
    private PlayerCharacterService playerCharacterService;

    @PostMapping("/add")
    public void add(@RequestBody PlayerCharacter playerCharacter) {
        playerCharacterService.savePlayerCharacter(playerCharacter);
    }

    @GetMapping("/getAll")
    public List<PlayerCharacter> list(){
        return playerCharacterService.getAllPlayerCharacters();
    }

    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable("id") Integer id) {
        playerCharacterService.removePlayerCharacter(id);
    }

    @GetMapping("/calc/{id}/{ac}")
    public String calc(@PathVariable("id") Integer id, @PathVariable("ac") int ac) {
        PlayerCharacter pc = playerCharacterService.getPlayerCharacter(id);
        return playerCharacterService.calculate(pc, ac);
    }

    @GetMapping("/calc2/{id}")
    public PlayerCharacter get(@PathVariable("id") Integer id) {
        return playerCharacterService.getPlayerCharacter(id);
        /*PlayerCharacter pc = playerCharacterService.getPlayerCharacter(id);
        double a = pc.getStandard_dam_mod();
        double b = pc.getFirst_hit_dam_mod();
        return playerCharacterService.calculate(id, a, b);*/
    }
}
