package com.kitap.agent.generate.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class JarValidator /*extends BaseClass*/ {


    /** checks a jar file is valid or not */
    public boolean isJarValid(String name, String path) throws IOException {
        Process process = Runtime.getRuntime().exec("java -jar " + name, null, new File(path));
        InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        line = reader.readLine();
        if (line == null) {
            throw new NullPointerException();
        } else {
            if (line.contains("no main manifest attribute")) {
                System.out.println(" The Given Jar File is valid ");
                return true;
            } else if (line.contains("Error: Invalid or corrupt jarfile")) {
                System.out.println(" The Given Jar File in INVALID, Pls Check");
                log.error(line);
                return false;
            } else {
                log.error(" Something went wrong ");
                return false;
            }
        }
    }
}
