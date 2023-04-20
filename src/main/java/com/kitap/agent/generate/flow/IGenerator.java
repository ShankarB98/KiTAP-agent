package com.kitap.agent.generate.flow;

import com.kitap.testresult.dto.agent.GenerationDetails;

/**
 * Interface used for generation of metadata
 * @author KT1450
 */
public interface IGenerator {
    /**
     * abstract method using for generation of metadata
     * @param details GenerationDetails object
     */
    void generate(GenerationDetails details);
}
