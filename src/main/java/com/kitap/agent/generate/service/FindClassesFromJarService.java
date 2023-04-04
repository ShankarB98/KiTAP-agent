package com.kitap.agent.generate.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitap.testresult.dto.agent.GenerationDetails;
import com.kitap.testresult.dto.generate.AUT;
import com.kitap.testresult.dto.generate.Clazz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.List;

@Slf4j
public class FindClassesFromJarService {

    /** parse the jar file and generates the data */
    public String parseJar(File jarFile, GenerationDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method parseJar started");
        AUT aut = new AUT();

        JarDetailsService jds = new JarDetailsService();
        JarParserService jps = new JarParserService();

        jds.getMetaData(aut, jarFile);
        List<Clazz> classes = jps.getAllClassData(jarFile, details);

        aut.setName(details.getAutName());
        aut.setDescription(details.getAutName());
        aut.setDisplayName(details.getAutName());
        aut.setVersion(details.getVersion());
        aut.setUrl("unknown");
        aut.setExecutableFilePath("unknown");
        aut.setType(details.getAutType());
        aut.setTestCases(classes);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("method parseJar completed with returning Json as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return gson.toJson(aut);
    }
}
