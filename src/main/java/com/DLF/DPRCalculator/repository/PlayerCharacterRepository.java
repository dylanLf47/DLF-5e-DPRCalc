package com.DLF.DPRCalculator.repository;

import com.DLF.DPRCalculator.model.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Integer> {
    List<PlayerCharacter> findByUsername(String username);

    void removeByUsername(String username);
}
