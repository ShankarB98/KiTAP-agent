package com.kitap.agent.generate.flow;


import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * IGenerator implemented class for generating the metadata
 * @author KT1450
 */
@Slf4j
public class Generator implements IGenerator{
    /**
     * Method helps in calling the metadata generation method
     * @param details GenerationDetails object
     */
    @Override
    public void generate(GenerationDetails details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("generate method using generationDetails as input");
        MetaDataGenerator generator = new MetaDataGenerator();
        generator.generateMetaData(details);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}