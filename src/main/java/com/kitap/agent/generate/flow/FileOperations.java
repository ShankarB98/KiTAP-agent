package com.kitap.agent.generate.flow;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileOperations {

    final String separator = File.separator;
    final String destinationPath = PropertyReaderHelper.getProperty("destinationpath");

    /** method for copying files to kitap destination path */
    public String copyFiles(GenerationDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        /** checking and adding kitap destination if not exists */
        createFolder(destinationPath);

        /** checking aut exists or not if not creating one */
        if (!checkAutExistence(details.getAutType(), details.getAutName()))
            checkAutExistence(details.getAutType(), details.getAutName());

        String qualifiedAutName = destinationPath+separator+details.getAutType()+separator+details.getAutName();

        /** getting last required version */
        String version = getLastVersionNumber(qualifiedAutName, details.getCreateNewVersion());
        String destination = qualifiedAutName+separator+version;

        /** copying files to destination path*/
        copy(details.getProjectPath().getAbsolutePath(), destination);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        //createStructureCopyFiles(details.getProjectPath().getAbsolutePath(), destination);
//        call(details.getProjectPath().getAbsolutePath(), destination);
        //return qualifiedAutName+separator+version;
        return version;
    }

    private boolean checkAutExistence(String autType, String autName){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        autType = Objects.requireNonNullElse(autType, "");
        autType = autType.equals("") ? "Web": autType;

        String autQualifiedType = destinationPath+separator+autType+separator+autName;
        File autFolder = new File(autQualifiedType);
        if (autFolder.exists()){
            log.info("aut folder path exists "+ autQualifiedType);
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return true;
        }
        else {
            log.info("aut folder created at "+ autQualifiedType);
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return autFolder.mkdir();
        }
    }

    private String getLastVersionNumber(String autPath, Boolean createNewVersion){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting last version number created by using autPath and createNewVersion as boolean");
        File autFolder = new File(autPath);

        File [] versions = autFolder.listFiles();
        assert versions != null;
        if (versions.length > 0){
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return getLastVersionNumber(versions, createNewVersion, autPath);
        }else {
            if(createFolder(autPath+separator+"1")) {
                stopWatch.stop();
                log.info("Execution time for " + new Object(){}.getClass().getEnclosingMethod().getName() +
                        " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
                return "1";
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return "";
    }

    private String getLastVersionNumber(File [] versions, Boolean createNewVersion, String autPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Arrays.sort(versions);
        if (createNewVersion){
            Long version = Long.parseLong(versions[versions.length-1].getName());
            version = version+1;
            createFolder(autPath+separator+String.valueOf(version));
            log.info("created new version folder with version number "+ version + " at path "+ autPath+separator+version);

            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return String.valueOf(version);
        }
        String versionName = versions[versions.length-1].getName();
        File delFile = new File (autPath+separator+versionName);
        deleteFolder(delFile);
        log.info("deleting files in existed version " + delFile);

        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return versionName;
    }

    private void copy(String source, String target) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("copying files by taking source and target paths as inputs");
        File directory = new File(source);
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    Path destinationFilePath = new File(target+separator+file.getName()).toPath();
                    copy(file.toPath(), destinationFilePath);
                }
                else if (file.isDirectory()) {
                    Path destinationFilePath = new File(target+separator+file.getName()).toPath();
                    copy(file.toPath(), destinationFilePath);

                    String destinationFolderAbsolutePath = target+separator+file.getName();
                    copy(file.getAbsolutePath(), destinationFolderAbsolutePath);
                }
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private void copy(Path source, Path target){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private List<File> getListOfFiles(File sourceDir){
        List<File> files = new ArrayList<>();
        File[] listOfFiles = sourceDir.listFiles();
        for (File file: listOfFiles){
            String fileName = file.getName();

            if(!(fileName.equals("src") || fileName.equals("target"))){
                files.add(file);
            }

        }
        for (File file: new File(sourceDir+separator+"target").listFiles()){
            if (file.getName().endsWith(".jar")){
                files.add(file);
            }
        }
        return  files;
    }


    private boolean createFolder(String path){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File version = new File(path);
        if (!version.exists()){
            log.info("folder created at path "+ path);
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return version.mkdir();
        }else{
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return false;
        }
    }

    public boolean createAut(String autName){
        autName = destinationPath+ File.separator + autName;
        File aut = new File(autName);
        if (!aut.exists()){
            log.info("folder created at path "+ autName);
            return aut.mkdir();
        }else return false;
    }

    public String [] getAuts(String autType){
        String autPath = destinationPath+separator+autType;
        File file = new File(autPath);
        if (!file.exists())
            file.mkdir();
        return getListOfVersions(autPath);
    }


    public String [] getListOfVersions(String autPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File autFolder = new File(autPath);
        if (autFolder.exists()){
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return getListOfVersions(autFolder.listFiles());
        }else{
            log.error("aut folder does not exists");
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return null;
    }
    private String [] getListOfVersions(File [] files){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting list of versions by taking array of files as input");
        List<String> list = new ArrayList<>();
        Arrays.sort(files);
        for (File file: files){
            if (file.isDirectory()){
                list.add(file.getName());
            }
        }
        String [] list1 = new String [list.size()];
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return list.toArray(list1);
    }

    private void deleteFolder(Path path){
        try {
            Files.delete(path);
            createFolder(path.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    private void deleteFolder(File file) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (File subfile : Objects.requireNonNull(file.listFiles())) {
            if (subfile.isDirectory()) {
                deleteFolder(subfile);
            }
            subfile.delete();
        }
        log.info("deleting folder by using file as input");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private void call(String source, String destinationPath){
        File directory = new File(source);
        File[] fList = directory.listFiles();
        for(File file: fList){
            if(file.isDirectory()){
                if(file.getName().equals("target")){
                    copyJars(source+separator+"target", destinationPath);
                }
                else if(file.getName().equals("src")){
                    String destin = destinationPath+separator+"src"+separator+"main"+separator+"resources";
                    File fil = new File(destin);
                    fil.mkdirs();
                    copyResources(source+separator+"src"+separator+"main"+separator+"resources", destin);
                }
            }
        }
    }

    private void copyJars(String source, String target){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("copying jars by using source and target paths as inputs");
        File targetDir = new File(source);
        File [] jarFiles = targetDir.listFiles();
        for(File file: jarFiles){
            if (file.isFile()) {
                if (file.getName().endsWith(".jar")){
                    Path destinationFilePath = new File(target+separator+file.getName()).toPath();
                    copy(file.toPath(), destinationFilePath);
                }
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
    private void createStructureCopyFiles(String source, String destinationPath){

        /*for (File file : fList) {
            if (file.isFile()) {
                if (file.getName().endsWith(".jar")){
                    Path destinationFilePath = new File(destinationPath+separator+file.getName()).toPath();
                    copy(file.toPath(), destinationFilePath);
                }
            }
            else if(file.isDirectory() && file.getName().equals("src")){
                Path destinationFilePath = new File(destinationPath+separator+file.getName()).toPath();
                copy(file.toPath(), destinationFilePath);

                String destinationFolderAbsolutePath = destinationPath+separator+file.getName();
                createStructureCopyFiles(file.getAbsolutePath(), destinationFolderAbsolutePath);
            }
        }*/
        //copyResources(new File(source+separator+"\\src\\main\\resources").listFiles(), destinationPath);

    }

    private void copyResources(String source, String destinationPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("copying resources by using source and destination paths as inputs");
        File directory = new File(source);
        File[] resources = directory.listFiles();
        for (File file: resources){
            if(file.isFile()){
                Path destinationFilePath = new File(destinationPath+separator+file.getName()).toPath();
                copy(file.toPath(), destinationFilePath);
            }
            else if (file.isDirectory()) {
                Path destinationFilePath = new File(destinationPath+separator+file.getName()).toPath();
                copy(file.toPath(), destinationFilePath);

                String destinationFolderAbsolutePath = destinationPath+separator+file.getName();
                copyResources(file.getAbsolutePath(), destinationFolderAbsolutePath);
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

}
