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
import java.util.Arrays;
import java.util.Objects;

/**
 * Class contains all the functionality related to File object like copying the files,
 * checking for any folder existence, getting the file/folder, creating new folders, deleting folder.
 * @author KT1450
 */
@Slf4j
public class FileOperations {

    final String separator = File.separator;
    final String destinationPath = PropertyReaderHelper.getProperty("destinationpath");

    /**
     *  method for copying files to kitap destination path
     * @param details GenerationDetails object
     * @return After copying returning the version
     */
    public String copyFiles(GenerationDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //checking and adding kitap destination if not exists
        createFolder(destinationPath);

        //checking aut exists or not if not creating one
        if (!checkAutExistence(details.getAutType(), details.getAutName()))
            checkAutExistence(details.getAutType(), details.getAutName());

        String qualifiedAutName = destinationPath+separator+details.getAutType()+separator+details.getAutName();

        // getting last required version
        String version = getLastVersionNumber(qualifiedAutName, details.getCreateNewVersion());
        String destination = qualifiedAutName+separator+version;

        // copying files to destination path
        copy(details.getProjectPath().getAbsolutePath(), destination);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return version;
    }

    /**
     * Checking whether AUT is present or not
     * @param autType type of AUT
     * @param autName name of AUT
     * @return true if already exists else creates new one
     */
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

    /**
     * Getting the last version number of AUT
     * @param autPath path of the AUT folder
     * @param createNewVersion true if new version needed else false
     * @return version name
     */
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

    /**
     * Getting the last version number of AUT
     * @param versions array of version files
     * @param createNewVersion true if new version needed else false
     * @param autPath path the aut folder
     * @return version name
     */
    private String getLastVersionNumber(File [] versions, Boolean createNewVersion, String autPath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Arrays.sort(versions);
        if (createNewVersion){
            Long version = Long.parseLong(versions[versions.length-1].getName());
            version = version+1;
            createFolder(autPath+separator+ version);
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

    /**
     * copying files from source path to target path
     * @param source path of the source folder
     * @param target path of the target folder
     */
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

    /**
     * copying files from source path to target path
     * @param source path of the source folder
     * @param target path of the target folder
     */
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

    /**
     * creating the version folder if not exists
     * @param path path for creating version folder
     * @return true if created new one else false
     */
    private boolean createFolder(String path) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File version = new File(path);
        if (!version.exists()) {
            log.info("folder created at path " + path);
            stopWatch.stop();
            log.info("Execution time for " + new Object() {
            }.getClass().getEnclosingMethod().getName() +
                    " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
            return version.mkdir();
        } else {
            stopWatch.stop();
            log.info("Execution time for " + new Object() {
            }.getClass().getEnclosingMethod().getName() +
                    " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
            return false;
        }
    }

    /**
     * Deleting the folder
     * @param file File object
     */
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
}