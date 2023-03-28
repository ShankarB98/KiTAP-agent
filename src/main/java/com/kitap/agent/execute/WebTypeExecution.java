package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class WebTypeExecution {

    final ExecutionHelper helper = new ExecutionHelper();
    final boolean singleOrMultiCase = true;
    final String separator = File.separator;
    final Properties properties = BaseClass.properties;

    /**
     * @Description executes web type projects
     * @param executionPath defines where test cases should execute
     * @param tests list of test cases to be executed
     * @return a list of processed test result
     * */
    public List<ExecutedTestCase> execute(String executionPath, List<String> tests){

        /** deleting previously existed serenity test result */
        helper.deleteTestResults(new File(executionPath+separator+"target"+separator+"site"));

        /** need to implement single test case execution*/
        //TODO need to implement single test case execution//
        //mvn clean verify -Dtags="name:appLoginTest"
        if(singleOrMultiCase){
            String command = properties.getProperty("mavenSerenityRunning");
            Process process = helper.getProcessor(command, new File(executionPath));

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
                Process process = helper.getProcessor(command, new File(executionPath));

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
        return adapter.obtainSerenityTestResult(executionPath+separator+"target"+separator+"site"+separator+"serenity");
    }
}
