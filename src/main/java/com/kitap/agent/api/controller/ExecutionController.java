package com.kitap.agent.api.controller;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.agent.database.service.AUTService;
import com.kitap.agent.database.service.ResultSaver;
import com.kitap.agent.execute.TestRunner;
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

    /**
     * @Description executes the test cases
     * @param details execution detail object
     * @return String
     * */
    @PostMapping("/execute")
    public String execute(@RequestBody ExecutionAutDetails details) {
        TestRunner runner = new TestRunner(details);
        ResultSaver saver = new ResultSaver(executedTestCaseRepository);
        saver.save(runner.executeTests(), details);
        return "saved to database";
    }

    /**
     * @Description saves aut
     * @param details aut object
     * @return String
     * */
    @PostMapping("/saveAut")
    public String saveAut(@RequestBody ApplicationUnderTest details){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.saveAutDetails(details);
    }

    /**
     * @Description returns list of aut's
     * @param autType execution detail object
     * @return String
     * */
    @GetMapping("/getAuts")
    public String [] getAuts(@RequestParam String autType){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAllAUT(autType);
    }

    /**
     * @Description returns list of aut types
     * @return String []
     * */
    @GetMapping("/getAutTypes")
    public String [] getAutTypes(){
        AUTService autService = new AUTService(applicationUnderTestRepo);
        return autService.getAutTypes();
    }

    /**
     * @Description delete an aut
     * @param invalidAuts - takes invalid auts which are not matched with the list fetched from database
     *                    and the list fetched from file system.
     * */
    @DeleteMapping("/deleteAUTs")
    public void deleteAUTs(@RequestBody List<String> invalidAuts){

    }

}
