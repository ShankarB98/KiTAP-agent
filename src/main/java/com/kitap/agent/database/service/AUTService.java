package com.kitap.agent.database.service;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.generate.base.BaseClass;
import com.kitap.agent.util.MachineInfo;
import com.kitap.agent.util.PropertyReader;
import com.kitap.testresult.dto.agent.MachineDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class AUTService extends BaseClass {

    private ApplicationUnderTestRepo repo;

    private final MachineInfo machineInfo = new MachineInfo();
    private final PropertyReader reader = new PropertyReader();

    public AUTService(ApplicationUnderTestRepo repo) {
        this.repo = repo;
    }

    public AUTService(){

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

    public String [] getAllAUT(String autType){
        List<String> allAutList = repo.getAllAUTNames(autType);
        return GetLitOfAutsThatAreOnFilesystem(allAutList, autType);
    }

    private String[] GetLitOfAutsThatAreOnFilesystem(List<String> auts, String autType)
    {
        ArrayList<String> validAuts = new ArrayList<>();
        // Check if the corresponding AUTs have code in the file system
        String baseAutPath = properties.getProperty("destinationpath")+separator+autType;
        for(String aut: auts)
        {
            String autPath = baseAutPath + separator + aut;
            File f = new File(autPath);
            if (f.exists()) {
                validAuts.add(aut);
            }
            else
            {
                log.warn("AUT: " + aut + " does not exist in this path: " + autPath);
                repo.deleteAUT(aut);
            }
        }
        if (validAuts.isEmpty()){
            return new String [0];
        }
        String [] validAutsArr = new String [validAuts.size()];
        return validAuts.toArray(validAutsArr);
    }

    public ApplicationUnderTest getAUT(String autName, String autType){
        MachineDetails details = machineInfo.getMachineInformation();
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

    public String [] getAutTypes(){
        List<String> allAutTypes = repo.getAutTypes();
        if (allAutTypes.isEmpty()){
            return new String [0];
        }
        String [] autTypes = new String [allAutTypes.size()];
        return allAutTypes.toArray(autTypes);
    }
}
