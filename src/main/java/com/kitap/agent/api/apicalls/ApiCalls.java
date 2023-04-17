package com.kitap.agent.api.apicalls;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.model.dto.AgentDto;
import com.kitap.agent.database.service.AUTService;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * All the api call methods are defined here by extending BaseApiCall class
 * @author KT1450
 */
@Component
@Slf4j
public class ApiCalls extends BaseApiCall {

    /**
     * Api call to know agent is registered or not
     * @param macAddress unique physical address of system which cannot change
     * @return boolean whether agent is registered or not
     */
    public boolean amIRegistered(String macAddress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("amIRegistered apicall started");
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+ PropertyReaderHelper.getProperty("am.i.registered") + "?macAddress=" + macAddress + "";
        log.info("mac address {}", macAddress);
        log.info("api call url {}", baseUrl);
        getResponse(macAddress, HttpMethod.GET);
        log.info("agent registration status {}", responseBody.getBody());
        log.info("amIRegistered apicall completed with returning boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (boolean) responseBody.getBody();
    }

    /**
     * Performs action to register agent
     * @param agentDto contains all agent details
     * @return boolean to know whether the registration is successful or not
     */
    public boolean register(AgentDto agentDto, String agentRegistrationKey) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("register apicall started");
        log.info(String.valueOf(agentDto));
        baseUrl = baseServerUrl+ PropertyReaderHelper.getProperty("agent.register")+"?key="+agentRegistrationKey+"";
        headers.set("key",agentRegistrationKey);
        getResponse(agentDto);
        log.info("agent registration status {}", responseBody.getStatusCode());
        log.info("register apicall completed with returning boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return responseBody.getStatusCode().value() == 201;
    }

    /**
     * Performs action to deregister agent
     * @param macAddress unique physical address of system which cannot change
     * @return boolean to know whether the deregistration is successful or not
     */
    public boolean deRegister(String macAddress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("deRegister apicall started");
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+ PropertyReaderHelper.getProperty("agent.deregister") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.PUT);
        log.info(String.valueOf(responseBody.getStatusCode()));
        log.info("deRegister apicall completed with returning boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    /**
     * saves aut in an in-memory database
     * @param details aut object
     * @return responsebody aut
     */
    private String saveAUT(ApplicationUnderTest details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("saveAUT apicall started");
        baseUrl = baseAgentUrl+ PropertyReaderHelper.getProperty("saveAUT");
        getResponse(details);
        log.info("saveAUT apicall completed with returning string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String) responseBody.getBody();
    }

    /**
     * Method returns list of aut under an aut type from in-memory database
     * @param autType aut type
     * @return String [] array of auts
     */
    public String[] getAllAUT(String autType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getAllAUT apicall started");
        autType = autType.replace(" ", "%20");
        baseUrl = baseAgentUrl+ PropertyReaderHelper.getProperty("getListOfAUT") + "?autType=" + autType + "";
        getResponse(autType);
        log.info("getAllAUT apicall completed with returning array of strings");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String[]) responseBody.getBody();
    }

    /**
     * Method executes the test cases
     * @param details execution details object
     */
    public void executeTests(ExecutionAutDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("executeTests apicall started");
        baseUrl = baseAgentUrl+ PropertyReaderHelper.getProperty("executeTests");
        getResponse(details);
        log.info((String) responseBody.getBody());
        log.info("executeTests apicall completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Method saves the aut with specified name and type
     * @param autName - The name of the AUT.
     * @param autType - The type of the AUT.
     * @return A string representing the result of saving the AUT.
     */
    public String saveAUT(String autName, String autType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("saveAUT method started");
        AUTService AUTService = new AUTService();
        log.info("saveAUT method completed with returning AUT details as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return saveAUT(AUTService.getAUT(autName, autType));
    }

    /**
     * Method returns list of aut types from in-memory database
     * @return A String array representing list of aut types
     */
    public String[] getAutTypes(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getAutTypes apicall started");
        baseUrl = baseAgentUrl+ PropertyReaderHelper.getProperty("getAutTypes");
        getResponse();
        log.info("getAutTypes apicall completed with returning array of AUT types");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String[]) responseBody.getBody();
    }
}