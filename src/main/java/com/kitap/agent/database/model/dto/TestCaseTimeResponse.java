package com.kitap.agent.database.model.dto;

import java.util.Date;

public interface TestCaseTimeResponse {
    Date getExecutedAt();
    Integer getCountResult();
}
