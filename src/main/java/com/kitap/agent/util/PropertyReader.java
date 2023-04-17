package com.kitap.agent.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Implemented class from IPropertyReader to get, load and read properties
 * @author KT1450
 */
@Slf4j
public class PropertyReader implements IPropertyReader{

    private Properties properties = new Properties();

    public PropertyReader(){
        log.info("property reader constructor loaded");
    }

    /**
     * Method returns matched key value
     * @param propertyName - used as key value
     * @return a String value of matched key
     */
    @Override
    public String getProperty(String propertyName) {
        return readProperty(propertyName);
    }

    /**
     * Reads value from properties for key value
     * @param propertyName - used as key value
     * @return a String value of matched key
     */
    private String readProperty(String propertyName){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("reading property by using propertyName as input");
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")){
            log.error("invalid property name #"+ propertyName);
            throw new RuntimeException("please enter valid property name");
        }
        loadProperties();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return this.properties.getProperty(propertyName);
    }

    /**
     * Method returns values from properties for key value
     * @param propertyNames - used as key value
     * @return a String array values of matched keys
     */
    @Override
    public List<String> getProperties(String[] propertyNames){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        loadProperties();
        List<String> properties = new ArrayList<>();
        for (String propertyName: propertyNames){
            String value = this.properties.getProperty(propertyName);
            if (value != null && !value.equals("")) {
                properties.add(value);
            }else{
                log.error("invalid property name #"+ propertyName);
                throw new RuntimeException("please enter valid property name");
            }
        }
        log.info("getting list of properties by using array of propertyNames");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return properties;
    }

    /**
     * Loads all properties into global variable
     * @return a Properties object that contains all property key value pairs
     */
    @Override
    public Properties loadProperties(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            this.properties.load(PropertyReader.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        log.info("loading properties and returning them");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return this.properties;
    }
}