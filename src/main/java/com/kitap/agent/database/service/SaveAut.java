package com.kitap.agent.database.service;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.ZonedDateTime;

import static java.io.File.separator;

@Component
@Slf4j
public class SaveAut {

    /**
     * Currently this class is not used
     * */

    @Autowired
    ApplicationUnderTestRepo repo;

    /**
     * @Description saves aut
     * @param autName - name of aut
     * @param autType - type of aut
     * @return a saved aut object
     * */
    public ApplicationUnderTest saveAut(String autName, String autType){
        saveAutDetails(getAUT(autName, autType));
        return repo.save(new ApplicationUnderTest());
    }


    public String saveAutDetails(ApplicationUnderTest aut){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("saving aut details with aut as input");
        ApplicationUnderTest applicationUnderTest = repo.isExists(aut.getName(), aut.getType());
        if (applicationUnderTest == null){
            repo.save(aut);
            log.info("New AUT created");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return "New AUT created";
        }else {
            log.warn("Duplicated AUT");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return "Duplicated AUT";
        }
    }

    private ApplicationUnderTest getAUT(String autName, String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ApplicationUnderTest aut = new ApplicationUnderTest();
        aut.setName(autName);
        aut.setDisplayName(autName);
        aut.setDescription(autName);
        aut.setExecutableFilePath(PropertyReaderHelper.getProperty("destinationpath")+separator+autType+separator+autName);
        /**
         * currently for demo purpose
         * setting sales force as default
         */
        aut.setType(autType);
        aut.setVersion(1);
        aut.setCreatedBy(PropertyReaderHelper.machineInformation.inetAddress.getHostName());
        aut.setCreatedAt(ZonedDateTime.now());
        aut.setIsActive(true);
        log.info("returning AUT by using autName and autType as inputs");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return aut;
    }
}