package com.kitap.agent.generate.service;

import com.kitap.testresult.dto.generate.AUT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.time.ZonedDateTime;

import java.io.File;
import java.io.IOException;

/**
 * Class contains functionality for giving all the details of jar
 * @author KT1450
 */
@Slf4j
public class JarDetailsService {


    /**
     * Method generates basic details about jar file
     * @param aut AUT object
     * @param jarFile jarfile as File object
     */
    public void getMetaData(AUT aut, File jarFile){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method getMetaData started with aut and jarfile");
        String jarFileName = jarFile.getName();
        String jarFilePath = jarFile.getAbsolutePath();
        JarValidator jarValidator = new JarValidator();
        boolean isJarValid;
        try {
            isJarValid = jarValidator.isJarValid(jarFileName, jarFilePath.substring(0, jarFilePath.lastIndexOf("\\")));
            if (!isJarValid)
                throw new RuntimeException("Invalid Jar File");
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        aut.setScanDate(ZonedDateTime.now().toString());
        aut.setLastModified(ZonedDateTime.now().toString());
        log.info("method getMetaData completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}