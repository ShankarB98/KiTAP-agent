package com.kitap.agent.generate.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitap.testresult.dto.agent.GenerationDetails;
import com.kitap.testresult.dto.generate.AUT;
import com.kitap.testresult.dto.generate.Clazz;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class FindClassesFromJarService {

    /** parse the jar file and generates the data */
    public String parseJar(File jarFile, GenerationDetails details){
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
        return gson.toJson(aut);
    }
}
