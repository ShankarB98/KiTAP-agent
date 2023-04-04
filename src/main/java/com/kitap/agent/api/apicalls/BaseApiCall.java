package com.kitap.agent.api.apicalls;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.model.dto.AgentDto;
import com.kitap.agent.util.PropertyReader;
import com.kitap.testresult.dto.agent.RegistrationDetails;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
@Slf4j
public abstract class BaseApiCall {
    RestTemplate restTemplate = new RestTemplate();
    URI uri;
    String baseUrl;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> request;
    ResponseEntity<?> responseBody;

    String baseServerUrl = BaseClass.properties.getProperty("server.base.url");
    String baseAgentUrl = BaseClass.properties.getProperty("agent.base.url");
    public BaseApiCall(){
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public void setBaseURI(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setBaseURI method started");
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        log.info("setBaseURI method completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(String reqValue, HttpMethod httpMethod){
        setBaseURI();
        setRequest(reqValue);
        responseBody = restTemplate.exchange(uri, httpMethod, request, Boolean.class);
    }

    protected void getResponse(String autType){
        setBaseURI();
        setRequest(autType);
        responseBody = restTemplate.exchange(uri, HttpMethod.GET, request, String[].class);
    }

    protected void getResponse(AgentDto agentDto){
        setBaseURI();
        setRequest(agentDto);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, void.class);
    }

    protected void getResponse(ExecutionAutDetails details){
        setBaseURI();
        setRequest(details);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, (Class<?>) String.class);
    }

    protected void getResponse(ApplicationUnderTest details){
        setBaseURI();
        setRequest(details);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }

    protected void getResponse(){
        setBaseURI();
        setRequest();
        responseBody = restTemplate.exchange(uri, HttpMethod.GET, request, String[].class);
    }

    public void setRequest(String macAddress){
        request = new HttpEntity<>(macAddress, headers);
    }
    public void setRequest(AgentDto agentDto){
        request = new HttpEntity<>(agentDto, headers);
    }

    public void setRequest(ExecutionAutDetails details){
        request = new HttpEntity<>(details, headers);
    }

    public void setRequest(ApplicationUnderTest details){
        request = new HttpEntity<>(details, headers);
    }

    public void setRequest(){
        request = new HttpEntity<>("", headers);
    }


    public void saveJson(String jsonData){
        setBaseURI();
        setRequest(jsonData);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, void.class);
    }

    public void setRequest(Class<?> jsonObject){
        request = new HttpEntity<>(jsonObject, headers);
    }

}
