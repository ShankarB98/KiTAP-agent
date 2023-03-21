package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.dto.ExecutedTestCase;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestRunner extends BaseClass {

    final String separator = File.separator;
    private final ExecutionAutDetails executionDetails;
    public TestRunner(ExecutionAutDetails executionDetails){
        this.executionDetails = executionDetails;
    }

    public List<ExecutedTestCase> executeTests(){
        TestExecution execution = new TestExecution();
        String testType = this.executionDetails.getTestType();
        String projectPath = properties.getProperty("destinationpath")+
                separator+testType+
                separator+this.executionDetails.getAut()+
                separator+this.executionDetails.getVersion();
        log.info("execution started at "+ projectPath);

        List<ExecutedTestCase> result = new ArrayList<>();

        switch (testType) {
            case "Web":
                result = execution.webExecution(projectPath, this.executionDetails.getTestCases());
                break;
            case "Sales Force":
                result = execution.sfExecution(projectPath, this.executionDetails.getTestCases());
                break;
            case "Mobile":
                break;
            case "API":
                result = execution.apiExecution(projectPath, this.executionDetails.getTestCases());
                break;
        }

        //TODO
        Reports reports = new Reports();
        reports.changeLogo(projectPath);
        return result;
    }
}
