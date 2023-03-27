package com.kitap.agent.generate.flow;


import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.generate.base.BaseClass;
import com.kitap.agent.generate.service.FindClassesFromJarService;
import com.kitap.testresult.dto.agent.GenerationDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class MetaDataGenerator extends BaseClass {
    
    /** method to generate test cases
     * jar path is jar file path
     * version Path is which version to create test cases
     * */

    ApiCalls apiCalls = new ApiCalls();

    public void generateMetaData(GenerationDetails details){
        String qualifiedPath = properties.getProperty("destinationpath")+separator+details.getAutType()+separator+details.getAutName()+separator+details.getVersion()+separator+"target";
        File [] jarFiles = new File(qualifiedPath).listFiles();
        if (jarFiles != null){
            for (File file:jarFiles) {
                if (file.getName().endsWith("-tests.jar")) {
                    details.setProjectPath(new File(file.getAbsolutePath()));
                }
//                if (file.getName().endsWith("-jar-with-dependencies.jar")) {
//                    details.setProjectPath(new File(file.getAbsolutePath()));
//                }
            }
        }

        String result = new FindClassesFromJarService().parseJar(details);
        System.out.println(result);

        File jsonFile = new File(generateJsonFileName(details));
        if (jsonFile.exists()){
            jsonFile.delete();
            log.info("json file already exists, deleted file");
        }
        try {
            jsonFile.createNewFile();
            Files.writeString(jsonFile.toPath(), result);
            apiCalls.saveJsonFileInServer(result);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    private String generateJsonFileName(GenerationDetails details){
        return properties.getProperty("destinationpath")+separator
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
