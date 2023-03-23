package com.kitap.agent.api.controller;

import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import com.kitap.agent.database.service.ResultSaver;
import com.kitap.agent.execute.TestRunner;
import com.kitap.agent.generate.util.FileOperations;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

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
        TestRunner runner = new TestRunner(details);
        ResultSaver saver = new ResultSaver(executedTestCaseRepository);
        saver.save(runner.executeTests(), details);
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
