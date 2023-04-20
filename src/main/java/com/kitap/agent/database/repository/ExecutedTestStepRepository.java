package com.kitap.agent.database.repository;

import com.kitap.agent.database.model.ExecutedTestStep;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * ExecutedTestStep Repository which is extending JpaRepository
 * @author KT1450
 */
public interface ExecutedTestStepRepository extends JpaRepository<ExecutedTestStep, Long> {
}
