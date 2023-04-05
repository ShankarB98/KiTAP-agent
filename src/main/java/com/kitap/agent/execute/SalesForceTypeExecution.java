package com.kitap.agent.execute;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.List;
@Slf4j
public class SalesForceTypeExecution {

    final ExecutionHelper helper = new ExecutionHelper();
    final boolean singleOrMultiCase = true;
    final String separator = File.separator;

    /**
     * @Description executes sales force type projects
     * @param projectDirectory defines where test cases should execute
     * @param tests list of test cases to be executed
     * */
    public List<ExecutedTestCase> sfExecution(String projectDirectory, List<String> tests){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        helper.ifReportsExists(projectDirectory);
        /**
         * commented this because we are not selecting any test cases
         * */
        //helper.createTestNGFile(projectDirectory, tests, "1");

        String command = PropertyReaderHelper.getProperty("mavenTestNgRunning");
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
        log.info("salesforce execution and returned list of executedTestCase objects");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return adapter.obtainTestNGTestResult(projectDirectory +separator+ PropertyReaderHelper.getProperty("testngreportsfilepath"));
    }
}
