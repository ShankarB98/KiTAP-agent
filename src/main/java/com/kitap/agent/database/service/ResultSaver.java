package com.kitap.agent.database.service;

import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.testresult.dto.ExecutedTestCase;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultSaver {

    private final ExecutedTestCaseRepository repo;

    public ResultSaver(ExecutedTestCaseRepository repo){
        this.repo = repo;
    }
    private final DtoToEntityConverter converter = new DtoToEntityConverter();

    public void save(List<ExecutedTestCase> tests, ExecutionAutDetails details){
        for (ExecutedTestCase tcase: tests){
            repo.save(converter.convertDtoToEntity(tcase, details));
            repo.findAll();
        }
    }
}
