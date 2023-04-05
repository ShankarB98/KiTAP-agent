package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
@Slf4j
public class Validator implements IValidator{
    @Override
    public String[] checkValidation(File projectPath) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ProjectValidator validator = new ProjectValidator();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return validator.check(projectPath);
    }

    @Override
    public void compileAndPackage(File projectDirectory) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CompileAndGenerateJarFile generate = new CompileAndGenerateJarFile();
        generate.compileAndPackage(projectDirectory);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    @Override
    public String copyFiles(GenerationDetails details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        FileOperations mover = new FileOperations();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return mover.copyFiles(details);
    }


}
