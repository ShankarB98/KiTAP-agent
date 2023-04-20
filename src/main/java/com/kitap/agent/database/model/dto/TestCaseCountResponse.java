package com.kitap.agent.database.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class containing fields testCaseName and countResult to give testCaseCountResponse
 * @author KT1450
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseCountResponse {
    private String testCaseName;
    private Long countResult;
}