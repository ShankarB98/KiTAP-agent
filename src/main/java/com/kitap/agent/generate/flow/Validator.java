package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;

/**
 * Class thet extends IValidator and helper in checking the validation of the project folder
 * @author KT1450
 */
@Slf4j
public class Validator implements IValidator {

    /**
     * Method that calls projectValidator method which validated the project folder
     *
     * @param projectPath path of the project folder
     * @return result after check in the form array
     */
    @Override
    public String[] checkValidation(File projectPath) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ProjectValidator validator = new ProjectValidator();
        stopWatch.stop();
        log.info("Execution time for " + new Object() {
        }.getClass().getEnclosingMethod().getName() +
                " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
        return validator.check(projectPath);
    }

    /**
     * Method helps in compiling and packaging the project folder
     *
     * @param projectDirectory project folder
     */
    @Override
    public void compileAndPackage(File projectDirectory) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CompileAndGenerateJarFile generate = new CompileAndGenerateJarFile();
        generate.compileAndPackage(projectDirectory);
        stopWatch.stop();
        log.info("Execution time for " + new Object() {
        }.getClass().getEnclosingMethod().getName() +
                " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
    }

    /**
     * Method helps in copying the files to specified folder
     *
     * @param details GenerationDetails object
     * @return returns version after copying
     */
    @Override
    public String copyFiles(GenerationDetails details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        FileOperations mover = new FileOperations();
        stopWatch.stop();
        log.info("Execution time for " + new Object() {
        }.getClass().getEnclosingMethod().getName() +
                " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
        return mover.copyFiles(details);
    }
}