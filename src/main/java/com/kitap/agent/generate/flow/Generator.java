package com.kitap.agent.generate.flow;


import com.kitap.testresult.dto.agent.GenerationDetails;

public class Generator implements IGenerator{
    @Override
    public void generate(GenerationDetails details) {
        MetaDataGenerator generator = new MetaDataGenerator();
        generator.generateMetaData(details);
    }
}
