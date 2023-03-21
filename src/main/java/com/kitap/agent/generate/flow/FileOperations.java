package com.kitap.agent.generate.flow;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;

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
public class FileOperations extends BaseClass {

    final String destinationPath = properties.getProperty("destinationpath");

    /** method for copying files to kitap destination path */
    public String copyFiles(GenerationDetails details){

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
        //createStructureCopyFiles(details.getProjectPath().getAbsolutePath(), destination);
//        call(details.getProjectPath().getAbsolutePath(), destination);
        //return qualifiedAutName+separator+version;
        return version;
    }

    private boolean checkAutExistence(String autType, String autName){
        autType = Objects.requireNonNullElse(autType, "");
        autType = autType.equals("") ? "Web": autType;

        String autQualifiedType = destinationPath+separator+autType+separator+autName;
        File autFolder = new File(autQualifiedType);
        if (autFolder.exists()){
            log.info("aut folder path exists "+ autQualifiedType);
            return true;
        }
        else {
            log.info("aut folder created at "+ autQualifiedType);
            return autFolder.mkdir();
        }
    }

    private String getLastVersionNumber(String autPath, Boolean createNewVersion){
        File autFolder = new File(autPath);

        File [] versions = autFolder.listFiles();
        assert versions != null;
        if (versions.length > 0){
            return getLastVersionNumber(versions, createNewVersion, autPath);
        }else {
            if(createFolder(autPath+separator+"1"))
                return "1";
        }
        return "";
    }

    private String getLastVersionNumber(File [] versions, Boolean createNewVersion, String autPath){
        Arrays.sort(versions);
        if (createNewVersion){
            Long version = Long.parseLong(versions[versions.length-1].getName());
            version = version+1;
            createFolder(autPath+separator+String.valueOf(version));
            log.info("created new version folder with version number "+ version + " at path "+ autPath+separator+version);
            return String.valueOf(version);
        }
        String versionName = versions[versions.length-1].getName();
        File delFile = new File (autPath+separator+versionName);
        deleteFolder(delFile);
        log.info("deleting files in existed version " + delFile);
        return versionName;
    }

    private void copy(String source, String target) {
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
    }

    private void copy(Path source, Path target){
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
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
        File version = new File(path);
        if (!version.exists()){
            log.info("folder created at path "+ path);
            return version.mkdir();
        }else return false;
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
        File autFolder = new File(autPath);
        if (autFolder.exists()){
            return getListOfVersions(autFolder.listFiles());
        }else{
            log.error("aut folder does not exists");
        }
        return null;
    }
    private String [] getListOfVersions(File [] files){
        List<String> list = new ArrayList<>();
        Arrays.sort(files);
        for (File file: files){
            if (file.isDirectory()){
                list.add(file.getName());
            }
        }
        String [] list1 = new String [list.size()];
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
        for (File subfile : Objects.requireNonNull(file.listFiles())) {
            if (subfile.isDirectory()) {
                deleteFolder(subfile);
            }
            subfile.delete();
        }
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
    }

}
