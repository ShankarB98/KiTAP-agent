package com.kitap.agent.generate.util;

import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileOperations extends BaseClass {

    public String[] getListOfFolders(String path){
        String kitapPath = properties.getProperty("destinationpath")+separator+path;
        File file = new File(kitapPath);
        if (file.exists()){
            return getListOfFolders(file.listFiles());
        }else{
            createNewFolder(kitapPath);
            log.error("aut type does not exists... created new one");
            return new String[0];
        }
    }

    private String[] getListOfFolders(File [] folders){
        List<String> autsList = new ArrayList<>();
        Arrays.sort(folders);
        for (File file: folders){
            if(file.isDirectory()){
                autsList.add(file.getName());
            }
        }
        String [] result = new String [autsList.size()];
        return autsList.toArray(result);
    }

    /** creates a folder at specified path*/
    public boolean createFolder(String autType, String autName){
        String finalPath = properties.getProperty("destinationpath")+separator+autType+separator+autName;
        return createNewFolder(finalPath);
    }

    private boolean createNewFolder(String path){
        File file = new File(path);
        if (file.exists()){
            return false;
        }
        return file.mkdir();
    }

    public void createAut(String autName, String autType){
        String path = properties.getProperty("destinationpath")+separator+autType;
        File file = new File(path);
        file.mkdir();
        path = path+separator+autName;
        file = new File(path);
        file.mkdir();
    }

}
