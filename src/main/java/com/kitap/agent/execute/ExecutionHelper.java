package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ExecutionHelper {

    final String separator = File.separator;
    Properties properties = BaseClass.properties;

    /**
     * @Description deletes previously existed test results from target folder
     * @param file - target folder path
     * */
    protected void deleteTestResults(File file){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("deleting test results by using file as input");
        if (file.exists()) {
            for (File subFile : file.listFiles()) {
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
     * @Description returns the process where command should run
     * @param command - command to execute
     * @param directory - location of where command is executing
     * @return Process - created process at a particular location
     * */
    protected Process getProcessor(String command, File directory){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting process by using command and filedirectory as inputs");
        Process process = null;
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
     * @Description converts the console prints to string and returns the same
     * @param process - Completed process which holding all the information
     * @return String - created process at a particular location
     * */
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
     * @Description handles the different types of exit values and through exception accordingly
     * @param process - Completed process which holding all the information
     * */
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
     * @Description writes log into defined path
     * @param projectDirectory - root folder to save logs
     * @param testCaseName - test case name to create log file
     * @param output - log content to save
     * */
    protected void writeLog(String projectDirectory, String testCaseName, String output){
        String logDirectory = projectDirectory + "\\library\\KiTAP\\reports-log";
        File dir = new File(logDirectory);
        dir.mkdirs();

        File logFile = new File(logDirectory+separator+testCaseName+".txt");
        Path fileName = Path.of(logDirectory+separator+testCaseName+".txt");

        if (logFile.exists()) {
            logFile.delete();
        }
        try {
            logFile.createNewFile();
            Files.writeString(fileName,String.valueOf(output));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description writes log into defined path without test case
     * @param projectDirectory - root folder to save logs
     * @param output - log content to save
     * */
    protected void writeLog(String projectDirectory, String output){
        String logDirectory = projectDirectory + "\\library\\KiTAP\\reports-log";
        createFolders(logDirectory, 1);
        File logFile = new File(logDirectory + "\\ExecutionLog.txt");
        Path fileName = Path.of(logDirectory + "\\ExecutionLog.txt");
        if (logFile.exists()) {
            logFile.delete();
        }
        createFile(logFile);
        try {
            Files.writeString(fileName, String.valueOf(output));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Deprecated this method is deprecated not using anymore
     * @Description generate commands to execute
     * @param testcaseName - test case name
     * @param testType - test type is used to define command
     * */
    protected String generateCommands(String testcaseName, String testType){
        if (testType.equals("web")){
            return "mvn.cmd verify";
        } else if (testType.equals("sf")) {

        } else if (testType.equals("mobile")) {

        }
        return "";
    }

    /**
     * @Description checks if reports are existed or not if exist then deletes the older one and create report file
     * @param projectDirectory - root project directory
     * */
    protected String ifReportsExists(String projectDirectory){
        String reportPath = projectDirectory +separator+properties.getProperty("testngreportsfilepath");
        createFolders(projectDirectory +separator+properties.getProperty("testngreportsfolderpath"), 0);
        File report = new File(reportPath);
        if (report.exists()){
            /*String dfile = projectDirectory + "\\BackupReportFiles\\AgentReport" + java.time.LocalDate.now() + ".json";
            createFolders(projectDirectory + "\\BackupReportFiles",0);
            createFile(dfile);
            try {
                FileReader fin = new FileReader(reportPath);
                FileWriter fout = new FileWriter(dfile, true);
                int c;
                while ((c = fin.read()) != -1) {
                    fout.write(c);
                }
                fin.close();
                fout.close();
                //Files.copy(sourceFilePath, targetFolderPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            if (report.delete()){
                createFile(report);
            }
        }else{
            createFile(report);
        }
        return "created successfully";
    }


    /**
     * @Description create testng xml file based on list of test cases
     * @param projectDirectory - project root directory
     * @param classNames - list of class names to be executed
     * */
    protected void createTestNGFile(String projectDirectory, List<String> classNames){
        File xmlFileNew = new File(projectDirectory + "\\src\\test\\resources\\all-tests-chrome.xml");
        Path xmlFilePath = Path.of(projectDirectory + "\\src\\test\\resources\\all-tests-chrome.xml");
        StringBuilder output = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(xmlFileNew));
            String st;
            boolean classStartFlag = false;
            boolean listenerExistFlag = false;
            while ((st = br.readLine()) != null) {
                String trimmedSt = st.trim();
                if (trimmedSt.equals("<listeners>")){
                    output.append(st+"\n");
                    continue;
                }
                else if(trimmedSt.equals("</listeners>")){
                    if(!listenerExistFlag){
                        output.append("\t\t<listener class-name=\"base.utils.Listeners.Listeners\"></listener>\n");
                    }
                    output.append(st+"\n");
                    continue;
                }
                else if (trimmedSt.equals("<classes>")){
                    output.append(st+"\n");
                    classStartFlag = true;
                    createFile(output, classNames);
                }
                else if (trimmedSt.equals("</classes>")) {
                    classStartFlag = false;
                }
                else if (trimmedSt.equals("<listener class-name=\"base.utils.Listeners.Listeners\"></listener>")){
                    listenerExistFlag = true;
                }
                if (!classStartFlag) {
                    output.append(st+"\n");
                }
            }
            if (xmlFileNew.exists()) {
                xmlFileNew.delete();
            }
            xmlFileNew.createNewFile();
            Files.writeString(xmlFilePath, output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description create testng xml file based on list of test cases
     * @param projectDirectory - project root directory
     * @param classNames - list of class names to be executed
     * @param version - version of aut
     * */
    protected void createTestNGFile(String projectDirectory, List<String> classNames, String version){
        String testngxmlfilepath = projectDirectory+separator+properties.getProperty("testngxmlfilepath");
        File xmlFileNew = new File(testngxmlfilepath);
        Path xmlFilePath = Path.of(testngxmlfilepath);
        StringBuilder output = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(xmlFileNew));
            String st;
            boolean classStartFlag = false;
            boolean listenerExistFlag = false;
            while ((st = br.readLine()) != null) {
                String trimmedSt = st.trim();
                switch (trimmedSt) {
                    case "<listeners>" -> {
                        output.append(st).append("\n");
                        continue;
                    }
                    case "</listeners>" -> {
                        if (!listenerExistFlag) {
                            output.append("\t\t<listener class-name=\"").append(properties.getProperty("listenerfilename")).append("\"/>\n");
                        }
                        output.append(st).append("\n");
                        continue;
                    }
                    case "<classes>" -> {
                        output.append(st).append("\n");
                        classStartFlag = true;
                        createFile(output, classNames);
                    }
                    case "</classes>" -> classStartFlag = false;
                    case "<listener class-name=\"com.kitap.utilities.listener\"></listener>" ->
                            listenerExistFlag = true;
                }
                if (!classStartFlag) {
                    output.append(st).append("\n");
                }
            }
            if (xmlFileNew.exists()) {
                xmlFileNew.delete();
            }
            xmlFileNew.createNewFile();
            Files.writeString(xmlFilePath, output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(StringBuilder output, List<String> classNames){
        StopWatch stopWatch= new StopWatch();
        stopWatch.start();
        for (String className: classNames){
            //output.append("\t\t\t<class name=\"base.AT.testscripts." + className + "\"/>\n");
            output.append("\t\t\t<class name=\"com.kitap.testscripts." + className + "\"/>\n");
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

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

    private void createFile(String path){
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
