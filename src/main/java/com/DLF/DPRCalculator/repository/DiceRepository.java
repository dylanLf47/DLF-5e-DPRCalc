package com.DLF.DPRCalculator.repository;

import com.DLF.DPRCalculator.model.Dice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiceRepository extends JpaRepository<Dice, Integer> {
}
