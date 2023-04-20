package com.kitap.agent.generate.util;

import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing all the functions performed with files and folders
 * @author KT1450
 */
@Slf4j
public class FileOperations {

    final String separator = File.separator;

    /**
     * Getting list of folders from the input path
     * @param path path from which we need to get folders
     * @return array of folders
     */
    public String[] getListOfFolders(String path){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting list of folders by using path as input");
        String kitapPath = PropertyReaderHelper.getProperty("destinationpath")+separator+path;
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

    /**
     * Getting list of folders from the array of folders as input
     * @param folders list of file objects
     * @return array of auts
     */
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

    /**
     * creating new folder at input path
     * @param path path where new folder have to create
     * @return false if folder exists else create the new folders and returns true
     */
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

    /**
     * Creating aut folder using autName and autType as inputs
     * @param autName name of the AUT
     * @param autType type of AUT
     */
    public void createAut(String autName, String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("creating autType folder and autName folder by using autName and autType as inputs");
        String path = PropertyReaderHelper.getProperty("destinationpath")+separator+autType;
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
