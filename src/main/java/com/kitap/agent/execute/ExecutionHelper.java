package com.kitap.agent.execute;

import com.kitap.agent.util.PropertyReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExecutionHelper {

    PropertyReader reader = new PropertyReader();

    final String separator = File.separator;
    protected void deleteTestResults(File file){
        if (file.exists()) {
            for (File subFile : file.listFiles()) {
                if (subFile.isDirectory()) {
                    deleteTestResults(subFile);
                }
                subFile.delete();
            }
        }
    }

    protected Process getProcessor(String command, File directory){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return process;
    }

    protected String processOutput(Process process){
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
        System.out.println(output);
        return String.valueOf(output);
    }

    protected void throwError(Process process){
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
    }

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


    protected String generateCommands(String testcaseName, String testType){
        if (testType.equals("web")){
            return "mvn.cmd verify";
        } else if (testType.equals("sf")) {

        } else if (testType.equals("mobile")) {

        }
        return "";
    }

    protected String ifReportsExists(String projectDirectory){
        String reportPath = projectDirectory +separator+reader.getProperty("testngreportsfilepath");
        createFolders(projectDirectory +separator+reader.getProperty("testngreportsfolderpath"), 0);
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

    protected void createTestNGFile(String projectDirectory, List<String> classNames, String version){
        String testngxmlfilepath = projectDirectory +separator+ reader.getProperty("testngxmlfilepath");
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
                            output.append("\t\t<listener class-name=\"").append(reader.getProperty("listenerfilename")).append("\"/>\n");
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
        for (String className: classNames){
            //output.append("\t\t\t<class name=\"base.AT.testscripts." + className + "\"/>\n");
            output.append("\t\t\t<class name=\"com.kitap.testscripts." + className + "\"/>\n");
        }
    }

    private void createFolders(String path, int number){
        File file = new File(path);
        if (number == 0){
            file.mkdir();
        }else {
            file.mkdirs();
        }
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
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
