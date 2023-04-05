package com.kitap.agent.generate.flow;


import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class CompileAndGenerateJarFile {

    /**
     * method for compile project and package
     * */
    public void compileAndPackage(File projectDirectory){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        getProcessor(BaseClass.getProperties(new String[] {"mavenvalidation","mavencompilation","mavenpackaging"}), projectDirectory);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private Process getProcessor(String[] commands, File directory){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commands,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return process;
    }

    private void getProcessor(List<String> properties, File directory){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting processor by using properties and directory as inputs");
        Process process = null;
        for (String command: properties){
            try {
                process = Runtime.getRuntime().exec(command,null, directory);
                processOutput(process);
                throwError(process);
            } catch (IOException e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private Process getProcessor(String command, File directory){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return process;
    }

    private void processOutput(Process process){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("output processing by using process object as input");
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        }
        catch(IOException e){
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        log.info(String.valueOf(output));
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private void throwError(Process process){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int exitValue;
        try {
            exitValue = process.waitFor();
            if (exitValue == 0) {
                log.info("processor exited code is 0");
            } else {
                log.info("Some thing abnormal has happened :(");
                log.info("processor exited code is "+ exitValue);
            }
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        } catch (InterruptedException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
