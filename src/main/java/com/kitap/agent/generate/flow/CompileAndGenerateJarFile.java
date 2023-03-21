package com.kitap.agent.generate.flow;


import com.kitap.agent.base.BaseClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class CompileAndGenerateJarFile extends BaseClass {

    /**
     * method for compile project and package
     * */
    public void compileAndPackage(File projectDirectory){
        getProcessor(getProperties(new String[] {"mavenvalidation","mavencompilation","mavenpackaging"}), projectDirectory);
    }

    private Process getProcessor(String[] commands, File directory){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commands,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return process;
    }

    private void getProcessor(List<String> properties, File directory){
        Process process = null;
        for (String command: properties){
            try {
                process = Runtime.getRuntime().exec(command,null, directory);
                processOutput(process);
                throwError(process);
            } catch (IOException e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
    }

    private Process getProcessor(String command, File directory){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command,null, directory);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return process;
    }

    private void processOutput(Process process){
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        }
        catch(IOException e){
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        System.out.println(output);
    }

    private void throwError(Process process){
        int exitValue;
        try {
            exitValue = process.waitFor();
            if (exitValue == 0) {
                log.info("processor exited code is 0");
            } else {
                log.info("Some thing abnormal has happened :(");
                log.info("processor exited code is "+ exitValue);
            }
        } catch (InterruptedException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
