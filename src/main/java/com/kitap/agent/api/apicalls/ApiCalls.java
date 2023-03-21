package com.kitap.agent.api.apicalls;



import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.service.AUTService;
import com.kitap.testresult.dto.agent.RegistrationDetails;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ApiCalls extends BaseApiCall {
    //String propertiesPath = reader.getProperty(ProjectType.CONSOLE);

    Logger logger = LoggerFactory.getLogger(ApiCalls.class);

    public boolean isRegister(String macAddress) {
        macAddress = macAddress.replace(" ", "%20");
        logger.info("just testing");
        baseUrl = reader.getProperty("isRegisterApi") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.GET);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    public boolean register(RegistrationDetails registrationDetails) {
        baseUrl = reader.getProperty("registerApi");
        getResponse(registrationDetails);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    public boolean deRegister(String macAddress) {
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = reader.getProperty("deRegisterApi") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.POST);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    public boolean quit(String macAddress) {
        macAddress = macAddress.replace(" ", "%20");
        baseUrl = reader.getProperty("quitApi") + "?macAddress=" + macAddress + "";
        getResponse(macAddress, HttpMethod.GET);
        return Boolean.TRUE.equals(responseBody.getBody());
    }

    public String saveAUT(ApplicationUnderTest details) {
        baseUrl = reader.getProperty("saveAUT");
        getResponse(details);
        return (String) responseBody.getBody();
    }

    public String[] getAllAUT(String autType) {
        autType = autType.replace(" ", "%20");
        baseUrl = reader.getProperty("getListOfAUT") + "?autType=" + autType + "";
        getResponse(autType);
        return (String[]) responseBody.getBody();
    }

    public void executeTests(ExecutionAutDetails details){
        baseUrl = reader.getProperty("executeTests");
        getResponse(details);
        System.out.println(responseBody.getBody());
    }

    public String saveAUT(String autName, String autType) {
        AUTService AUTService = new AUTService();
        return saveAUT(AUTService.getAUT(autName, autType));
    }

    public String[] getAutTypes(){
        baseUrl = reader.getProperty("getAutTypes");
        getResponse();
        return (String[]) responseBody.getBody();
    }

    public void saveJsonFileInServer(String jsonData){
        baseUrl = reader.getProperty("saveJson");
        saveJson(jsonData);
    }


}
