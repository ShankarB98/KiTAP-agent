package com.kitap.agent.api.apicalls;

import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.model.dto.AgentDto;
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

    String baseServerUrl = PropertyReaderHelper.getProperty("server.base.url");
    String baseAgentUrl = PropertyReaderHelper.getProperty("agent.base.url");
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(reqValue);
        responseBody = restTemplate.exchange(uri, httpMethod, request, Boolean.class);
        log.info("getting response by using requestValue as string and httpmethod");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(String autType){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(autType);
        responseBody = restTemplate.exchange(uri, HttpMethod.GET, request, String[].class);
        log.info("getting response by using autType as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(AgentDto agentDto){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(agentDto);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, void.class);
        log.info("getting response by using agentDto");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(ExecutionAutDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(details);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, (Class<?>) String.class);
        log.info("getting response by using executionAutDetails");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(ApplicationUnderTest details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(details);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        log.info("getting response by using AutDetails");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    protected void getResponse(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest();
        responseBody = restTemplate.exchange(uri, HttpMethod.GET, request, String[].class);
        log.info("getting response");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void setRequest(String macAddress){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request = new HttpEntity<>(macAddress, headers);
        log.info("setting request using macAddress");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
    public void setRequest(AgentDto agentDto){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request = new HttpEntity<>(agentDto, headers);
        log.info("setting request using agentDto");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void setRequest(ExecutionAutDetails details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request = new HttpEntity<>(details, headers);
        log.info("setting request using executionAutDetails");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void setRequest(ApplicationUnderTest details){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request = new HttpEntity<>(details, headers);
        log.info("setting request using AutDetails");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void setRequest(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request = new HttpEntity<>("", headers);
        log.info("setting request");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }


    public void saveJson(String jsonData){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setBaseURI();
        setRequest(jsonData);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, void.class);
        log.info("saving Json using jsonData as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void setRequest(Class<?> jsonObject){
        request = new HttpEntity<>(jsonObject, headers);
    }

}
