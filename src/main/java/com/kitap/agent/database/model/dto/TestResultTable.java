package com.kitap.agent.database.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
/**
 * Class containing fields testCase, result and executedTime for TestResultTable
 * @author KT1450
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResultTable {
    private String testCase;
    private String result;
    private ZonedDateTime executedTime;
}
