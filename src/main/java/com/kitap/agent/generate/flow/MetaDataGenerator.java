package com.kitap.agent.generate.flow;


import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.base.BaseClass;
import com.kitap.agent.generate.service.FindClassesFromJarService;
import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class MetaDataGenerator {

    final String separator = File.separator;

    ApiCalls apiCalls = new ApiCalls();

    public void generateMetaData(GenerationDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("generating metadata by using generation details as input");
        //String qualifiedPath = properties.getProperty("destinationpath")+separator+details.getAutType()+separator+details.getAutName()+separator+details.getVersion()+separator+"target";
        String qualifiedPath = details.getProjectPath()+separator+"target";
        File [] jarFiles = new File(qualifiedPath).listFiles();
        File jarFile = null;
        if (jarFiles != null){
            for (File file:jarFiles) {
                if (file.getName().endsWith("-tests.jar")) {
                    jarFile = file;
                }
//                if (file.getName().endsWith("-jar-with-dependencies.jar")) {
//                    details.setProjectPath(new File(file.getAbsolutePath()));
//                }
            }
        }

        String result = new FindClassesFromJarService().parseJar(jarFile, details);
        log.info(result);

        File jsonFile = new File(generateJsonFileName(details));
        if (jsonFile.exists()){
            jsonFile.delete();
            log.info("json file already exists, deleted file");
        }
        try {
            jsonFile.createNewFile();
            Files.writeString(jsonFile.toPath(), result);
            log.info("metadata generation completed");
            //apiCalls.saveJsonFileInServer(result);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    private String generateJsonFileName(GenerationDetails details){
        log.info("generating json file name is returned from generateJsonFileName method");
        return BaseClass.properties.getProperty("destinationpath")+separator
                            +details.getAutType()+separator
                            +details.getAutName()+separator
                            +details.getVersion()+separator
                            +details.getAutName()+".json";
    }

    private String formatJsonFileName(String jsonFileName){
        if(jsonFileName.endsWith(".json")){
            System.out.println("from if");
            System.out.println(jsonFileName);
            return jsonFileName;
        }else{
            System.out.println(jsonFileName);
            System.out.println("from else");
            return jsonFileName+".json";
        }
    }
}
