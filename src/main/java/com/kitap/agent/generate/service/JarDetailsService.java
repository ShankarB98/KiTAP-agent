package com.kitap.agent.generate.service;

import com.kitap.testresult.dto.generate.AUT;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;

import java.io.File;
import java.io.IOException;

@Slf4j
public class JarDetailsService {


    /** generates basic details about jar file */
    public void getMetaData(AUT aut, File jarFile){
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
    }
}
