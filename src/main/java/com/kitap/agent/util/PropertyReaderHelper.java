package com.kitap.agent.util;

import com.kitap.agent.ui.machineInfo.MachineInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * This class will do loading properties from Property reader class and holds those values in a variable
 *       And provides information when ever requested instead of reading values from application.properties file
 *       all the time.
 *       Holds information and references of classes and information which required over the project.
 * @author KT1450
 */
@Slf4j
public class PropertyReaderHelper {
    static final private Properties properties = new PropertyReader().loadProperties();
    static final public MachineInformation machineInformation = new MachineInformation();

    /**
     * Method returns single property from already loaded properties
     * @param propertyName name of the property
     * @return  property value(String)
     */
    public static String getProperty(String propertyName){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting property by using propertyName as input");
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")){
            log.error("invalid property name #"+ propertyName);
            throw new RuntimeException("please enter valid property name");
        }
        log.info("returning the property value");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return properties.getProperty(propertyName);
    }

    /**
     * Method returns list of properties from already loaded properties
     * @param propertyNames array of property names to get property values
     * @return List of property values corresponding to property names
     */
    public static List<String> getProperties(String[] propertyNames){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("getting properties by using array of propertyNames as input");
        List<String> properties = new ArrayList<>();
        for (String propertyName: propertyNames){
            String value = PropertyReaderHelper.properties.getProperty(propertyName);
            if (value != null && !value.equals("")) {
                properties.add(value);
            }else{
                log.error("invalid property name #"+ propertyName);
                throw new RuntimeException("please enter valid property name");
            }
        }
        log.info("returning the list of properties");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return properties;
    }

    public static String updatePropertyValue(boolean value){
        return (String) properties.setProperty("isServerLess", String.valueOf(value));
    }
}