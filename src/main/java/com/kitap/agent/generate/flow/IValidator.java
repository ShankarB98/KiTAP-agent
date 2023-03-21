package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;

import java.io.File;

public interface IValidator {
    String[] checkValidation(File projectPath);
    void compileAndPackage(File projectDirectory);
    String copyFiles(GenerationDetails details);
}
