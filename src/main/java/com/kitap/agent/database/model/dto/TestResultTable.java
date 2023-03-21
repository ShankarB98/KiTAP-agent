package com.kitap.agent.database.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResultTable {
    private String testCase;
    private String result;
    private ZonedDateTime executedTime;
}
