package com.kitap.agent.api.controller;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.agent.database.service.AUTService;
import com.kitap.agent.database.service.ResultSaver;
import com.kitap.agent.execute.TestRunner;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for operating the execution of tests
 * @author  KT1450
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class ExecutionController {
    @Autowired
    ExecutedTestCaseRepository executedTestCaseRepository;
    @Autowired
    ApplicationUnderTestRepo applicationUnderTestRepo;

    /**
     * Method executes the test cases
     * @param details execution detail object
     * @return String - saved to database
     */
    @PostMapping("/execute")
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
     * Method saves aut
     * @param details aut object
     * @return message from saveAutDetails method of autService class
     */
    @PostMapping("/saveAut")
    public String saveAut(@RequestBody ApplicationUnderTest details){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.saveAutDetails(details);
    }

    /**
     * Method returns list of aut's
     * @param autType execution detail object
     * @return String[] - array of auts
     */
    @GetMapping("/getAuts")
    public String [] getAuts(@RequestParam String autType){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAllAUT(autType);
    }

    /**
     * Method returns list of aut types
     * @return String [] - array of aut types
     */
    @GetMapping("/getAutTypes")
    public String [] getAutTypes(){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAutTypes();
    }
}