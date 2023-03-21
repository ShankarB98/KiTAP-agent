package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;

import java.io.File;

public class Validator implements IValidator{
    @Override
    public String[] checkValidation(File projectPath) {
        ProjectValidator validator = new ProjectValidator();
        return validator.check(projectPath);
    }

    @Override
    public void compileAndPackage(File projectDirectory) {
        CompileAndGenerateJarFile generate = new CompileAndGenerateJarFile();
        generate.compileAndPackage(projectDirectory);
    }

    @Override
    public String copyFiles(GenerationDetails details) {
        FileOperations mover = new FileOperations();
        return mover.copyFiles(details);
    }


}
