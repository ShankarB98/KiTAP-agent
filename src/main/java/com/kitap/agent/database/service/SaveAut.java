package com.kitap.agent.database.service;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

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
        ApplicationUnderTest applicationUnderTest = repo.isExists(aut.getName(), aut.getType());
        if (applicationUnderTest == null){
            repo.save(aut);
            log.info("New AUT created");
            return "New AUT created";
        }else {
            log.warn("Duplicated AUT");
            return "Duplicated AUT";
        }
    }

    private ApplicationUnderTest getAUT(String autName, String autType){
        ApplicationUnderTest aut = new ApplicationUnderTest();
        aut.setName(autName);
        aut.setDisplayName(autName);
        aut.setDescription(autName);
        aut.setExecutableFilePath(BaseClass.properties.getProperty("destinationpath")+BaseClass.separator+autType+BaseClass.separator+autName);
        /**
         * currently for demo purpose
         * setting sales force as default
         */
        aut.setType(autType);
        aut.setVersion(1);
        aut.setCreatedBy(BaseClass.machineInformation.inetAddress.getHostName());
        aut.setCreatedAt(ZonedDateTime.now());
        aut.setIsActive(true);
        return aut;
    }
}