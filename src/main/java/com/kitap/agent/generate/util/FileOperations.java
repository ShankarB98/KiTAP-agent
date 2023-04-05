package com.kitap.agent.generate.util;

import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class FileOperations {

    final Properties properties = BaseClass.properties;
    final String separator = File.separator;

    public String[] getListOfFolders(String path){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting list of folders by using path as input");
        String kitapPath = properties.getProperty("destinationpath")+separator+path;
        File file = new File(kitapPath);
        if (file.exists()){
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return getListOfFolders(file.listFiles());
        }else{
            createNewFolder(kitapPath);
            log.error("aut type does not exists... created new one");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return new String[0];
        }
    }

    private String[] getListOfFolders(File [] folders){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<String> autsList = new ArrayList<>();
        Arrays.sort(folders);
        for (File file: folders){
            if(file.isDirectory()){
                autsList.add(file.getName());
            }
        }
        String [] result = new String [autsList.size()];
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return autsList.toArray(result);
    }

    /** creates a folder at specified path*/
    public boolean createFolder(String autType, String autName){
        String finalPath = properties.getProperty("destinationpath")+separator+autType+separator+autName;
        return createNewFolder(finalPath);
    }

    private boolean createNewFolder(String path){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File file = new File(path);
        if (file.exists()){
            log.info("folder already exists");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return false;
        }
        log.info("created new folder and returned");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return file.mkdir();
    }

    public void createAut(String autName, String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("creating autType folder and autName folder by using autName and autType as inputs");
        String path = properties.getProperty("destinationpath")+separator+autType;
        File file = new File(path);
        file.mkdir();
        path = path+separator+autName;
        file = new File(path);
        file.mkdir();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}
