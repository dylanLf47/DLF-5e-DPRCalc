package com.DLF.DPRCalculator.controller;

import com.DLF.DPRCalculator.model.PlayerCharacter;
import com.DLF.DPRCalculator.service.PlayerCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player_character")
@CrossOrigin
//@CrossOrigin(origins = "https://dlf-5e-dpr-calculator.onrender.com")
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

    @GetMapping("/get/{username}")
    public List<PlayerCharacter> getByUsername(@PathVariable("username") String username){
        return playerCharacterService.getPlayerCharactersByUsername(username);
    }

    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable("id") Integer id) {
        playerCharacterService.removePlayerCharacter(id);
    }

    @Transactional
    @DeleteMapping("/removeByUser/{username}")
    public void remove(@PathVariable("username") String username) {
        playerCharacterService.removePlayerCharacterByUsername(username);
    }

    @GetMapping("/calc/{id}/{ac}")
    public String calc(@PathVariable("id") Integer id, @PathVariable("ac") int ac) {
        try {
            PlayerCharacter pc = playerCharacterService.getPlayerCharacter(id);
            return playerCharacterService.calculate(pc, ac);
        } catch (Exception e) {
            return "There was an error processing your request";
        }
    }
}
