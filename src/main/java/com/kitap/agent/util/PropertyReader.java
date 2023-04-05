package com.kitap.agent.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertyReader implements IPropertyReader{

    final String rootDir = System.getProperty("user.dir")+"\\src\\main\\resources\\application.properties";
    Properties properties = new Properties();

    public PropertyReader(){
        log.info("property reader constructor loaded");
    }


    /**
     * @Description returns matched key value
     * @param propertyName - used as key value
     * @return a String value of matched key
     * */
    @Override
    public String getProperty(String propertyName) {
        return readProperty(propertyName);
    }

    /**
     * @Description reads value from properties for key value
     * @param propertyName - used as key value
     * @return a String value of matched key
     * */
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
     * @Description returns values from properties for key value
     * @param propertyNames - used as key value
     * @return a String array values of matched keys
     * */
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
     * @Description loads all properties into global variable
     * @return a Properties object that contains all property key value pairs
     * */
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



    public File getResourceAsFile(String resourceName) {
        File file;
        try{
            file = new File(removeSymbols(resourceName));
        }catch(NullPointerException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }

        return file;
    }

    private String removeSymbols(String resourceName){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("removing symbols by using resourceName as input");
        String path = Thread.currentThread()
                .getContextClassLoader()
                .getResource(stripLeadingSlash(resourceName))
                .getFile()
                .replaceAll("%20"," ");
        path = path.startsWith("file:\\") ? path.substring(6) : path;
        //path = path.replace("")
        log.info("removeSymbols method is returning path as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return path;
    }

    private static String stripLeadingSlash(String resourceName) {
        return (resourceName.startsWith("/")) ? resourceName.substring(1) : resourceName;
    }


}
