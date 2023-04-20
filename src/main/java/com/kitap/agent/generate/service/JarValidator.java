package com.kitap.agent.generate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class contains logic which validates the jar file
 * @author KT1497
 */
@Slf4j
public class JarValidator  {

    /**
     * checks a jar file is valid or not
     * @param name name of the jar file
     * @param path path of the jar file
     * @return true if valid else false
     * @throws IOException exception by exec method, readLine method
     */
    public boolean isJarValid(String name, String path) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method isJarValid started with jarfile name and path");
        Process process = Runtime.getRuntime().exec("java -jar " + name, null, new File(path));
        InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        line = reader.readLine();
        if (line == null) {
            log.error("null");
            throw new NullPointerException();
        } else {
            if (line.contains("no main manifest attribute")) {
                log.info(" The Given Jar File is valid ");
                log.info("method isJarValid completed with returning true");

                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                return true;
            } else if (line.contains("Error: Invalid or corrupt jarfile")) {
                log.info(" The Given Jar File in INVALID, Pls Check");
                log.error(line);
                log.info("method isJarValid completed with returning false");

                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                return false;
            } else {
                log.error(" Something went wrong ");
                log.info("method isJarValid completed with returning false");

                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                return false;
            }
        }
    }
}