package com.kitap.agent.generate.flow;

import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.io.File.separator;

@Slf4j
public class ProjectValidator {

    //String propertiesPath = reader.getProperty(ProjectType.TESTEXECUTION);

    /** method for check project validation */
    public String[] check(File projectPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("checking project by using projectPath as input");
        String value = isValidProject(projectPath);
        log.info("value at"+value);
        String [] arr = new String[2];
        switch (value) {
            case "main and test java does not exists":
            case "main and test java exists but not required packages":
            case "pom file does not exists":
            case "it is not a valid kitap project":
                log.info(value);
                arr[0] = "invalid";
                break;
            case "Web":
            case "Sales Force":
            case "Desktop":
            case "Mobile":
            case "API":
                arr[0] = "valid";
                arr[1] = value;
                break;
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return arr;
    }

    private String isValidProject(File projectPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("checking valid project or not by using projectpath as input");
        File dir = projectPath.getAbsoluteFile();

        File subDirMain = new File(dir, PropertyReaderHelper.getProperty("mainapplicationpath"));
        File subDirTest = new File(dir, PropertyReaderHelper.getProperty("testapplicationpath"));
        File subDirTestResources = new File(dir,PropertyReaderHelper.getProperty("testresourcespath"));
        if(subDirMain.isDirectory()&&subDirTest.isDirectory()&&subDirTestResources.isDirectory()){
            log.info("main and test directories are present in project");

            File testCaseDir = new File(subDirTest, PropertyReaderHelper.getProperty("testcasespackage"));
            File stepsDir = new File(subDirTest, PropertyReaderHelper.getProperty("stepspackage"));
            File pagesDir = new File(subDirTest, PropertyReaderHelper.getProperty("pagespackage"));
            File utilitiesDir = new File(subDirTest, PropertyReaderHelper.getProperty("utilitiespackage"));
            File baseDir = new File(subDirTest, PropertyReaderHelper.getProperty("basepackage"));

            File driversDir = new File(subDirTestResources + separator + "drivers");
            File testDataDir = new File(subDirTestResources + separator + "testdata");
            File healeniumPropertiesFile = new File(subDirTestResources + separator + "healenium.properties");
            File serenityPropertiesFile = new File(dir + separator + "serenity.properties");
            File kairosLogoOnSerenityName = new File(dir + separator + "serenity-logo.png");


            if(testCaseDir.isDirectory()&&stepsDir.isDirectory()&&
                    pagesDir.isDirectory()&&utilitiesDir.isDirectory()&&baseDir.isDirectory()&&
                    driversDir.isDirectory()&&testDataDir.isDirectory()&&healeniumPropertiesFile.exists()&&
                    serenityPropertiesFile.exists()&&kairosLogoOnSerenityName.exists()) {
                log.info("All the specified packages and files are present");

                File pomFile = new File(dir + separator + "pom.xml");
                if (pomFile.exists()) {
                    stopWatch.stop();
                    log.info("Execution time for " + new Object() {
                    }.getClass().getEnclosingMethod().getName() +
                            " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
                    return projectAutType(pomFile);
                } else {
                    stopWatch.stop();
                    log.info("Execution time for " + new Object() {
                    }.getClass().getEnclosingMethod().getName() +
                            " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
                    return "pom file does not exists";
                }
            } else {
                stopWatch.stop();
                log.info("Execution time for " + new Object() {
                }.getClass().getEnclosingMethod().getName() +
                        " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
                return "main and test java exists but not required packages";
            }
        }else{
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return "main and test java does not exists";
        }
    }

    private String isKiTAPProject(File pomFile){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pomFile));
            String line;
            String trimmedLine;
            while ((line = reader.readLine()) != null) {
                trimmedLine = line.trim();
                if (trimmedLine.contains("<groupId>com.kitap.fw.core</groupId>")) {
                    trimmedLine = trimmedLine.replace(" ", "");
                    //if (trimmedLine.contains("<!--")) continue;
                    return "it is kitap core project";
                }
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        projectAutType(pomFile);
        return "it is not kitap project";
    }

    private String projectAutType(File pomFile){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting project autType by using pomfile as input");
        final String GROUPID_WEB =  "<groupId>com.kitap.fw.web</groupId>";
        final String GROUPID_SF = "<groupId>com.kitap.fw.salesforce</groupId>";
        final String GROUPID_MOBILE = "<groupId>com.kitap.fw.mobile</groupId>";
        final String GROUPID_DESKTOP = "<groupId>com.kitap.fw.desktop</groupId>";
        final String GROUPID_API = "<groupId>com.kitap.fw.api</groupId>";

        final String KITAP_CORE = "<groupId>com.kitap.fw.core</groupId>";

        boolean isKitapCore = false;
        boolean containsPlatform = false;
        String value = "";

        try{
            BufferedReader reader = new BufferedReader(new FileReader(pomFile));
            String line;
            String trimmedLine;
            while ((line = reader.readLine()) != null) {
                trimmedLine = line.trim();
                if (trimmedLine.contains(KITAP_CORE)) {
                    //trimmedLine = trimmedLine.replace(" ", "");
                    //if (trimmedLine.contains("<!--")) continue;
                    isKitapCore = true;
                }
                if(trimmedLine.contains(GROUPID_WEB)){
                    containsPlatform = true;
                    value = "Web";
                } else if (trimmedLine.contains(GROUPID_SF)) {
                    containsPlatform = true;
                    value = "Sales Force";
                }else if (trimmedLine.contains(GROUPID_MOBILE)) {
                    containsPlatform = true;
                    value = "Mobile";
                }else if (trimmedLine.contains(GROUPID_DESKTOP)) {
                    containsPlatform = true;
                    value = "Desktop";
                }else if (trimmedLine.contains((GROUPID_API))){
                    containsPlatform = true;
                    value = "API";
                }
            }
            if (isKitapCore && containsPlatform){
                log.info("returned project autType");
                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                return value;
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        log.info("not able to return autType because not a valid kitap project");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return "it is not a valid kitap project";
    }
}
