package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.dto.ExecutedTestCase;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestRunner {

    /**
     * This class identifies aut type and initiates the tests' execution accordingly by processing inputs
     * */

    final String separator = File.separator;
    private final ExecutionAutDetails executionDetails;

    /**
     * Used class level variable to hold execution detail object
     * That was done by using constructor
     * */
    public TestRunner(ExecutionAutDetails executionDetails){
        this.executionDetails = executionDetails;
    }

    /**
     * @Description  invokes the test cases' execution process and returns test result
     * @return a list of executed test cases results
     * */
    public List<ExecutedTestCase> executeTests(){
        //TestExecution execution = new TestExecution();
        String testType = this.executionDetails.getTestType();
        String executionPath = BaseClass.properties.getProperty("destinationpath")+
                separator+testType+
                separator+this.executionDetails.getAut()+
                separator+this.executionDetails.getVersion();
        log.info("execution started at "+ executionPath);

        List<ExecutedTestCase> result = new ArrayList<>();

        switch (testType) {
            case "Web":
                result = new WebTypeExecution().execute(executionPath, this.executionDetails.getTestCases());
                break;
            case "Sales Force":
                result = new SalesForceTypeExecution().sfExecution(executionPath, this.executionDetails.getTestCases());
                break;
            case "Mobile":
                break;
            case "API":
                result = new APITypeExecution().apiExecution(executionPath, this.executionDetails.getTestCases());
                break;
        }

        //TODO
        Reports reports = new Reports();
        reports.changeLogo(executionPath);
        return result;
    }
}
