package com.kitap.agent.database.service;

import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.testresult.dto.ExecutedTestCase;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultSaver {

    /**
     * @Description saves the executed result into in-memory database
     */

    private final ExecutedTestCaseRepository repo;
    private final DtoToEntityConverter converter = new DtoToEntityConverter();

    public ResultSaver(ExecutedTestCaseRepository repo) {
        this.repo = repo;
    }

    /**
     * @Description save method to save test cases in in-memory database
     * @param tests - list of test cases that to save in database
     * @param details - used to fill some test case fields
     */
    public void save(List<ExecutedTestCase> tests, ExecutionAutDetails details) {
        for (ExecutedTestCase tcase : tests) {
            repo.save(converter.convertDtoToEntity(tcase, details));
            repo.findAll();
        }
    }
}
