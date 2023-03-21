package com.kitap.agent.database.repository;

import com.kitap.agent.database.model.ExecutedTestStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutedTestStepRepository extends JpaRepository<ExecutedTestStep, Long> {
}
