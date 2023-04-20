package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;

import java.io.File;
/**
 * Interface used for validating, compiling, packaging and copying files and folders to target directory
 * @author KT1450
 */
public interface IValidator {
    /**
     * abstract method used for checking the validation of the selected project
     * @param projectPath path of the project
     * @return array with valid/invalid in zeroth index and string value in first index
     */
    String[] checkValidation(File projectPath);

    /**
     * abstract method used for compiling and packaging the test project
     * @param projectDirectory folder of the project
     */
    void compileAndPackage(File projectDirectory);

    /**
     * copying the generated files to specified folder
     * @param details GenerationDetails object
     * @return version name
     */
    String copyFiles(GenerationDetails details);
}
