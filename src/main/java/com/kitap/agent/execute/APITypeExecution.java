package com.kitap.agent.execute;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.List;
@Slf4j
public class APITypeExecution {

    final ExecutionHelper helper = new ExecutionHelper();
    final boolean singleOrMultiCase = true;
    final String separator = File.separator;
    /**
     * @Description executes api type projects
     * @param projectDirectory defines where test cases should execute
     * @param tests list of test cases to be executed
     * */
    public List<ExecutedTestCase> apiExecution(String projectDirectory, List<String> tests){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        /** deleting previously existed serenity test result */
        helper.deleteTestResults(new File(projectDirectory+separator+"target"+separator+"site"));

        /** need to implement single test case execution*/
        //TODO need to implement single test case execution//
        //mvn clean verify -Dtags="name:appLoginTest"
        if(singleOrMultiCase){
            String command = PropertyReaderHelper.getProperty("mavenSerenityRunning");
            Process process = helper.getProcessor(command, new File(projectDirectory));

            /**
             * getting output from processor
             * */
            String output = helper.processOutput(process);
            log.info(output);

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
                String command = PropertyReaderHelper.getProperty("mavenSerenityRunning");
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
        log.info("completed apiexecution method with returning list of executedTestCase objects");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return adapter.obtainSerenityTestResult(projectDirectory+separator+"target"+separator+"site"+separator+"serenity");
    }
}
