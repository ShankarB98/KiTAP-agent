package com.kitap.agent.api.apicalls;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.model.dto.AgentDto;
import com.kitap.agent.database.service.AUTService;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Properties;

@Component
@Slf4j
public class ApiCalls extends BaseApiCall {

    Properties properties = BaseClass.properties;

    public boolean isActive(String macAddress) {
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+ properties.getProperty("agent.inactive") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.POST);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    /**
     * @Description api call to know agent is registered or not
     * @param macAddress unique physical address of system which cannot change
     * @return boolean
     * */
    public boolean amIRegistered(String macAddress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("amIRegistered apicall started");
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+properties.getProperty("am.i.registered") + "?macAddress=" + macAddress + "";
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
     * @Description Performs action to register agent
     * @param agentDto contains all agent details
     * @return boolean
     * */
    public boolean register(AgentDto agentDto, String agentRegistrationKey) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("register apicall started");
        log.info(String.valueOf(agentDto));
        baseUrl = baseServerUrl+properties.getProperty("agent.register")+"?key="+agentRegistrationKey+"";
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
     * @Description Performs action to deregister agent
     * @param macAddress unique physical address of system which cannot change
     * @return boolean
     * */
    public boolean deRegister(String macAddress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("deRegister apicall started");
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+properties.getProperty("agent.deregister") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.PUT);
        log.info(String.valueOf(responseBody.getStatusCode()));
        log.info("deRegister apicall completed with returning boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    /**
     * @Description Performs action to shut down agent
     * @param macAddress unique physical address of system which cannot change
     * @return boolean
     * */
    public boolean quit(String macAddress) {
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = baseServerUrl+properties.getProperty("quitApi") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.GET);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    /**
     * @Description Performs action to save AUT in server
     * @param details AUT Object
     * @return String
     * */
    public String saveAutInServer(ApplicationUnderTest details){
        baseUrl = baseServerUrl+properties.getProperty("saveAutInServer");
        getResponse(details);
        return (String) responseBody.getBody();
    }

    /**
     * @Description returns list of aut under an aut type from server
     * @param autType aut type
     * @return String []
     * */
    public String[] getAllAUTFromServer(String autType) {
        autType = autType.replace(" ", "%20");
        baseUrl = baseServerUrl+properties.getProperty("getListOfAUT") + "?autType=" + autType + "";
        getResponse(autType);
        return (String[]) responseBody.getBody();
    }

    /**
     * @Description saves generated json for test cases data in server
     * @param jsonData json data
     * */
    public void saveJsonFileInServer(String jsonData){
        baseUrl = baseServerUrl+properties.getProperty("saveJson");
        saveJson(jsonData);
    }
    public void getJsonFileInServer(String jsonData){
        baseUrl = baseServerUrl+properties.getProperty("getJsonFileFromServer");
        saveJson(jsonData);
    }

    /**
     * @Description saves aut in in-memory data base
     * @param details aut object
     * */
    public String saveAUT(ApplicationUnderTest details) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("saveAUT apicall started");
        baseUrl = baseAgentUrl+properties.getProperty("saveAUT");
        getResponse(details);
        log.info("saveAUT apicall completed with returning string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String) responseBody.getBody();
    }


    /**
     * @Description returns list of aut under an aut type from in-memory database
     * @param autType aut type
     * @return String []
     * */
    public String[] getAllAUT(String autType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getAllAUT apicall started");
        autType = autType.replace(" ", "%20");
        baseUrl = baseAgentUrl+properties.getProperty("getListOfAUT") + "?autType=" + autType + "";
        getResponse(autType);
        log.info("getAllAUT apicall completed with returning array of strings");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String[]) responseBody.getBody();
    }

    /**
     * @Description executes the test cases
     * @param details execution details object
     * */
    public void executeTests(ExecutionAutDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("executeTests apicall started");
        baseUrl = baseAgentUrl+properties.getProperty("executeTests");
        getResponse(details);
        log.info((String) responseBody.getBody());
        log.info("executeTests apicall completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * @Description saves the aut with specified name and type
     * @param autName - The name of the AUT.
     * @param autType - The type of the AUT.
     * @return A string representing the result of saving the AUT.
     * */
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
     * @Description returns list of aut types from in-memory database
     * @return A String array representing list of aut types
     * */
    public String[] getAutTypes(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getAutTypes apicall started");
        baseUrl = baseAgentUrl+properties.getProperty("getAutTypes");
        getResponse();
        log.info("getAutTypes apicall completed with returning array of AUT types");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return (String[]) responseBody.getBody();
    }
}
