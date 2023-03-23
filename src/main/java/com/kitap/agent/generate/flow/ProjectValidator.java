package com.kitap.agent.generate.flow;

import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class ProjectValidator {

    //String propertiesPath = reader.getProperty(ProjectType.TESTEXECUTION);

    /** method for check project validation */
    public String[] check(File projectPath){
        String value = isValidProject(projectPath);
        System.out.println("value at"+value);
        String [] arr = new String[2];
        switch (value) {
            case "main and test java does not exists":
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
        return arr;
    }

    private String isValidProject(File projectPath){
        File dir = projectPath.getAbsoluteFile();
        File subDirMain = new File(dir, BaseClass.properties.getProperty("mainapplicationpath"));
        File subDirTest = new File(dir, BaseClass.properties.getProperty("testapplicationpath"));
        if(subDirMain.isDirectory()&&subDirTest.isDirectory()){
            File pomFile = new File(dir+BaseClass.separator+"pom.xml");
            if (pomFile.exists()) {
                return projectAutType(pomFile);
            }else return "pom file does not exists";
        }else return "main and test java does not exists";
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
                return value;
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        return "it is not a valid kitap project";
    }


}
