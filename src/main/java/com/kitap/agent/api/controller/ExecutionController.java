package com.kitap.agent.api.controller;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.agent.database.service.AUTService;
import com.kitap.agent.database.service.ResultSaver;
import com.kitap.agent.execute.execution.TestRunner;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ExecutionController {
    @Autowired
    ExecutedTestCaseRepository executedTestCaseRepository;

    @Autowired
    ApplicationUnderTestRepo applicationUnderTestRepo;

    @PostMapping("/execute")
    public String execute(@RequestBody ExecutionAutDetails details) {
        TestRunner runner = new TestRunner(details);
        ResultSaver saver = new ResultSaver(executedTestCaseRepository);
        saver.save(runner.executeTests(), details);
        return "saved to database";
    }

    @PostMapping("/saveAut")
    public String saveAut(@RequestBody ApplicationUnderTest details){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.saveAutDetails(details);
    }

    @GetMapping("/getAuts")
    public String [] getAuts(@RequestParam String autType){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAllAUT(autType);
    }

    @GetMapping("/getAutTypes")
    public String [] getAutTypes(){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAutTypes();
    }

    @DeleteMapping("/deleteAUTs")
    public void deleteAUTs(@RequestBody List<String> invalidAuts){

    }

}
