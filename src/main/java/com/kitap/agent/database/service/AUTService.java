package com.kitap.agent.database.service;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.repository.ApplicationUnderTestRepo;
import com.kitap.agent.ui.machineInfo.MachineInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

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

    /**
     * @Description fetches all aut types from aut table
     * @param autType - aut type to get all aut's
     * @return list of count response test result table
     * */
    public String [] getAllAUT(String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<String> allAutList = repo.getAllAUTNames(autType);
        log.info("returning array of auts by using autType as input");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return GetLitOfAutsThatAreOnFilesystem(allAutList, autType);
    }

    /**
     * @Description returns list of auts from file system that matches with database
     * @param autType - aut type to get all aut's
     * @return returns list of auts from file system
     * */
    private String[] GetLitOfAutsThatAreOnFilesystem(List<String> auts, String autType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
        log.info("returning array of auts that are on filesystem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return validAuts.toArray(validAutsArr);
    }

    /**
     * @Description returns aut object
     * @param autType - the type of the aut
     * @param autName - the name of the aut
     * @return returns aut object
     * */
    public ApplicationUnderTest getAUT(String autName, String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
        log.info("returning AUT by using autName and autType as inputs");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return aut;
    }

    /**
     * @Description provides list of aut types
     * @return A String of array contains aut types
     * */
    public String [] getAutTypes(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<String> allAutTypes = repo.getAutTypes();
        if (allAutTypes.isEmpty()){
            log.info("No autType present");
            return new String [0];
        }
        String [] autTypes = new String [allAutTypes.size()];
        log.info("returning array of autTypes");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return allAutTypes.toArray(autTypes);
    }
}
