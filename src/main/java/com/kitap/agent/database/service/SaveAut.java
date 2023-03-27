package com.kitap.agent.database.service;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.generate.base.BaseClass;
import com.kitap.agent.util.MachineInfo;
import com.kitap.testresult.dto.agent.MachineDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Slf4j
public class SaveAut extends BaseClass {

    @Autowired
    ApplicationUnderTestRepo repo;

    private final MachineInfo info = new MachineInfo();

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
        MachineDetails details = info.getMachineInformation();
        ApplicationUnderTest aut = new ApplicationUnderTest();
        aut.setName(autName);
        aut.setDisplayName(autName);
        aut.setDescription(autName);
        aut.setExecutableFilePath(properties.getProperty("destinationpath")+separator+autType+separator+autName);
        /**
         * currently for demo purpose
         * setting sales force as default
         */
        aut.setType(autType);
        aut.setVersion(1);
        aut.setCreatedBy(details.getHostName());
        aut.setCreatedAt(ZonedDateTime.now());
        aut.setIsActive(true);
        return aut;
    }


}
