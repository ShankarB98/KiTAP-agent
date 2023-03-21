package com.kitap.agent.execute.execution;

import com.kitap.agent.execute.base.BaseClass;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class TestExecution extends BaseClass {

    final boolean singleOrMultiCase = true;

    public List<ExecutedTestCase> webExecution(String projectDirectory, List<String> tests){
        ExecutionHelper helper = new ExecutionHelper();
        /** deleting previously existed serenity test result */
        helper.deleteTestResults(new File(projectDirectory+separator+"target"+separator+"site"));

        /** need to implement single test case execution*/
        //TODO need to implement single test case execution//
        //mvn clean verify -Dtags="name:appLoginTest"
        if(singleOrMultiCase){
            String command = properties.getProperty("mavenSerenityRunning");
            Process process = helper.getProcessor(command, new File(projectDirectory));

            /**
             * getting output from processor
             * */
            String output = helper.processOutput(process);

            /** commented this line as of now we are not writing any logs for web execution
             * 9th Feb 2023
             * */
            //helper.writeLog(projectDirectory, testCaseName, String.valueOf(output));

            /** method for exiting processor*/
            helper.throwError(process);

        }
        else {
            for (String testCaseName : tests) {
                // String command = "mvn.cmd verify -Dtags=\"name:"+testCaseName+"\"";
                String command = properties.getProperty("mavenSerenityRunning");
                Process process = helper.getProcessor(command, new File(projectDirectory));

                /**
                 * getting output from processor
                 * */
                String output = helper.processOutput(process);

                /** commented this line as of now we are not writing any logs for web execution
                 * 9th Feb 2023
                 * */
                //helper.writeLog(projectDirectory, testCaseName, String.valueOf(output));

                /** method for exiting processor*/
                helper.throwError(process);

            }
        }

        /** Converting result into base format */
        ConvertedResult adapter = new ConvertedResult();
        return adapter.obtainSerenityTestResult(projectDirectory+separator+"target"+separator+"site"+separator+"serenity");
    }

    public List<ExecutedTestCase> sfExecution(String projectDirectory, List<String> tests){
        ExecutionHelper helper = new ExecutionHelper();
        helper.ifReportsExists(projectDirectory);
        /**
         * commented this because we are not selecting any test cases
         * */
        //helper.createTestNGFile(projectDirectory, tests, "1");

        String command = properties.getProperty("mavenTestNgRunning");
        Process process = helper.getProcessor(command, new File(projectDirectory));

        /**
         * getting output from processor
         * */
        String output = helper.processOutput(process);

        /**
         * commented this line as of now we are not writing any logs for sales force execution
         * 15th Dec 2022
         * */
        //helper.writeLog(projectDirectory, String.valueOf(output));

        /** method for exiting processor*/
        helper.throwError(process);

        /**
         * extracting final executed reports
         * */
        ConvertedResult adapter = new ConvertedResult();
        return adapter.obtainTestNGTestResult(projectDirectory +separator+ properties.getProperty("testngreportsfilepath"));
    }

    public List<ExecutedTestCase> apiExecution(String projectDirectory, List<String> tests){
        ExecutionHelper helper = new ExecutionHelper();
        /** deleting previously existed serenity test result */
        helper.deleteTestResults(new File(projectDirectory+separator+"target"+separator+"site"));

        /** need to implement single test case execution*/
        //TODO need to implement single test case execution//
        //mvn clean verify -Dtags="name:appLoginTest"
        if(singleOrMultiCase){
            String command = properties.getProperty("mavenSerenityRunning");
            Process process = helper.getProcessor(command, new File(projectDirectory));

            /**
             * getting output from processor
             * */
            String output = helper.processOutput(process);
            System.out.println(output);

            /** commented this line as of now we are not writing any logs for web execution
             * 9th Feb 2023
             * */
            //helper.writeLog(projectDirectory, testCaseName, String.valueOf(output));

            /** method for exiting processor*/
            helper.throwError(process);

        }
        else {
            for (String testCaseName : tests) {
                // String command = "mvn.cmd verify -Dtags=\"name:"+testCaseName+"\"";
                String command = properties.getProperty("mavenSerenityRunning");
                Process process = helper.getProcessor(command, new File(projectDirectory));

                /**
                 * getting output from processor
                 * */
                String output = helper.processOutput(process);

                /** commented this line as of now we are not writing any logs for web execution
                 * 9th Feb 2023
                 * */
                //helper.writeLog(projectDirectory, testCaseName, String.valueOf(output));

                /** method for exiting processor*/
                helper.throwError(process);

            }
        }

        /** Converting result into base format */
        ConvertedResult adapter = new ConvertedResult();
        return adapter.obtainSerenityTestResult(projectDirectory+separator+"target"+separator+"site"+separator+"serenity");
    }
}
