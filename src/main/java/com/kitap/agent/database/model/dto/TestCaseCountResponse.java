package com.kitap.agent.database.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseCountResponse {
    private String testCaseName;
    private Long countResult;
}
