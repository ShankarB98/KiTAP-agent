package com.kitap.agent.api.apicalls;

import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.util.PropertyReader;
import com.kitap.testresult.dto.agent.RegistrationDetails;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class BaseApiCall {
    RestTemplate restTemplate = new RestTemplate();
    URI uri;
    String baseUrl;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> request;
    ResponseEntity<?> responseBody;
    PropertyReader reader = new PropertyReader();
    public BaseApiCall(){
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public void setBaseURI(){
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void getResponse(String reqValue, HttpMethod httpMethod){
        setBaseURI();
        setRequest(reqValue);
        responseBody = restTemplate.exchange(uri, httpMethod, request, (Class<?>) Boolean.class);
    }

    protected void getResponse(String autType){
        setBaseURI();
        setRequest(autType);
        responseBody = restTemplate.exchange(uri, HttpMethod.GET, request, String[].class);
    }

    protected void getResponse(RegistrationDetails details){
        setBaseURI();
        setRequest(details);
        responseBody = restTemplate.exchange(uri, HttpMethod.POST, request, (Class<?>) Boolean.class);
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
    public void setRequest(RegistrationDetails registrationDetails){
        request = new HttpEntity<>(registrationDetails, headers);
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

}
