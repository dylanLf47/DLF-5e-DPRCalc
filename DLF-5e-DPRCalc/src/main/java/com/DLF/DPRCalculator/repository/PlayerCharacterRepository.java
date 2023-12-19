package com.DLF.DPRCalculator.repository;

import com.DLF.DPRCalculator.model.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Integer> {
}
