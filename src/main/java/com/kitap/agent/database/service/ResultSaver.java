package com.kitap.agent.database.service;

import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.testresult.dto.ExecutedTestCase;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
@Slf4j
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (ExecutedTestCase tcase : tests) {
            repo.save(converter.convertDtoToEntity(tcase, details));
            repo.findAll();
        }
        log.info("saving tests");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}
