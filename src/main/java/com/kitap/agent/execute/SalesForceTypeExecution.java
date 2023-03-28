package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class SalesForceTypeExecution {

    final ExecutionHelper helper = new ExecutionHelper();
    final boolean singleOrMultiCase = true;
    final String separator = File.separator;
    final Properties properties = BaseClass.properties;

    /**
     * @Description executes sales force type projects
     * @param projectDirectory defines where test cases should execute
     * @param tests list of test cases to be executed
     * */
    public List<ExecutedTestCase> sfExecution(String projectDirectory, List<String> tests){
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
}
