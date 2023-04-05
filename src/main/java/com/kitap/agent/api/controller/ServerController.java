package com.kitap.agent.api.controller;

import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.agent.database.service.ResultSaver;
import com.kitap.agent.execute.TestRunner;
import com.kitap.agent.generate.util.FileOperations;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.io.File;
@Slf4j
@RestController
@RequestMapping("/v1")
public class ServerController {

    @Autowired
    ExecutedTestCaseRepository executedTestCaseRepository;

    final FileOperations operations = new FileOperations();

    /**
     * @Description executes the test cases and saves that result into database
     * @param details execution detail object
     * */
    @PostMapping("/executeTests")
    public String execute(@RequestBody ExecutionAutDetails details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("execution API started with executionAutDetails");
        TestRunner runner = new TestRunner(details);
        ResultSaver saver = new ResultSaver(executedTestCaseRepository);
        saver.save(runner.executeTests(), details);
        log.info("execution API completed with returning saved to database message");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return "saved to database";
    }

    /**
     * @Description returns list of versions
     * @param autName aut name
     * @param autType aut type
     * @return String[]
     * */
    @GetMapping("/getVersions")
    public String[] getVersions(@RequestParam String autType, @RequestParam String autName){
        autType = autType.replace("%20", " ");
        autName = autName.replace("%20", " ");
        return operations.getListOfFolders(autType+ File.separator+autName);
    }
}
