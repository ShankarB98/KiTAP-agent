package com.kitap.agent.database.service;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.ui.machineInfo.MachineInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class AUTService{

    private ApplicationUnderTestRepo repo;

    private final MachineInformation machineInfo = new MachineInformation();

    public AUTService(ApplicationUnderTestRepo repo) {
        this.repo = repo;
    }

    public AUTService(){

    }

    /**
     * @Description saves aut
     * @param aut - uses this aut entity to save aut object
     * @return returns the response of about aut creation
     * */
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

    /**
     * @Description fetches all aut types from aut table
     * @param autType - aut type to get all aut's
     * @return list of count response test result table
     * */
    public String [] getAllAUT(String autType){
        List<String> allAutList = repo.getAllAUTNames(autType);
        return GetLitOfAutsThatAreOnFilesystem(allAutList, autType);
    }

    /**
     * @Description returns list of auts from file system that matches with database
     * @param autType - aut type to get all aut's
     * @return returns list of auts from file system
     * */
    private String[] GetLitOfAutsThatAreOnFilesystem(List<String> auts, String autType)
    {
        ArrayList<String> validAuts = new ArrayList<>();
        // Check if the corresponding AUTs have code in the file system
        String baseAutPath = BaseClass.properties.getProperty("destinationpath")+BaseClass.separator+autType;
        for(String aut: auts)
        {
            String autPath = baseAutPath + BaseClass.separator + aut;
            File f = new File(autPath);
            if (f.exists()) {
                validAuts.add(aut);
            }
            else
            {
                log.warn("AUT: {}" + aut + " does not exist in this path: " + autPath);
                repo.deleteAUT(aut);
                log.info(aut+" deleted from aut table");
            }
        }
        if (validAuts.isEmpty()){
            return new String [0];
        }
        String [] validAutsArr = new String [validAuts.size()];
        return validAuts.toArray(validAutsArr);
    }

    /**
     * @Description returns aut object
     * @param autType - the type of the aut
     * @param autName - the name of the aut
     * @return returns aut object
     * */
    public ApplicationUnderTest getAUT(String autName, String autType){
        ApplicationUnderTest aut = new ApplicationUnderTest();
        aut.setName(autName);
        aut.setDisplayName(autName);
        aut.setDescription(autName);
        aut.setExecutableFilePath(BaseClass.properties.getProperty("destinationpath")+BaseClass.separator+autType+BaseClass.separator+autName);
        aut.setType(autType);
        aut.setVersion(1);
        aut.setCreatedBy(BaseClass.machineInformation.inetAddress.getHostName());
        aut.setCreatedAt(ZonedDateTime.now());
        aut.setIsActive(true);
        return aut;
    }

    /**
     * @Description provides list of aut types
     * @return A String of array contains aut types
     * */
    public String [] getAutTypes(){
        List<String> allAutTypes = repo.getAutTypes();
        if (allAutTypes.isEmpty()){
            return new String [0];
        }
        String [] autTypes = new String [allAutTypes.size()];
        return allAutTypes.toArray(autTypes);
    }
}
