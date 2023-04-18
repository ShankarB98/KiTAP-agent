package com.kitap.agent.execute;

import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class contains functionality that helps in the execution of tests
 * @author KT1450
 */
@Slf4j
public class ExecutionHelper {

    final String separator = File.separator;

    /**
     * Method deletes previously existed test results from target folder
     * @param file - target folder path
     */
    protected void deleteTestResults(File file){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("deleting test results by using file as input");
        if (file.exists()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                if (subFile.isDirectory()) {
                    deleteTestResults(subFile);
                }
                subFile.delete();
            }
        }
        log.info("deleting test results completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Method returns the process where command should run
     * @param command - command to execute
     * @param directory - location of where command is executing
     * @return Process - created process at a particular location
     */
    protected Process getProcessor(String command, File directory){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting process by using command and filedirectory as inputs");
        Process process;
        try {
            process = Runtime.getRuntime().exec(command,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        log.info("getting process completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return process;
    }

    /**
     * Method converts the console prints to string and returns the same
     * @param process - Completed process which holding all the information
     * @return String - created process at a particular location
     */
    protected String processOutput(Process process){
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
        output.append(LocalDateTime.now());
        log.info("Processed output {}",(output));
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return String.valueOf(output);
    }

    /**
     * Method handles the different types of exit values and through exception accordingly
     * @param process - Completed process which holding all the information
     */
    protected void throwError(Process process){
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
        } catch (InterruptedException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Method checks if reports are existed or not if exist then deletes the older one and create report file
     * @param projectDirectory - root project directory
     * @return message - created successfully
     */
    protected String ifReportsExists(String projectDirectory){
        String reportPath = projectDirectory +separator+ PropertyReaderHelper.getProperty("testngreportsfilepath");
        createFolders(projectDirectory +separator+ PropertyReaderHelper.getProperty("testngreportsfolderpath"), 0);
        File report = new File(reportPath);
        if (report.exists()){
            if (report.delete()){
                createFile(report);
            }
        }else{
            createFile(report);
        }
        return "created successfully";
    }

    /**
     * creating folder(s) by using path and number as inputs
     * @param path path to create folder
     * @param number number of folders
     */
    private void createFolders(String path, int number){
        StopWatch stopWatch= new StopWatch();
        stopWatch.start();
        File file = new File(path);
        if (number == 0){
            file.mkdir();
        }else {
            file.mkdirs();
        }
        log.info("created folders by using path and number as input");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * creating new file by using file as input
     * @param file File object of which new file has to create
     */
    private void createFile(File file){
        StopWatch stopWatch= new StopWatch();
        stopWatch.start();
        try {
            file.createNewFile();
            log.info("created new file by using file object as input");
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}