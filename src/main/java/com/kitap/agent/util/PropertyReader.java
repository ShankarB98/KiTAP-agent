package com.kitap.agent.util;

import lombok.extern.slf4j.Slf4j;

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


    @Override
    public String getProperty(String propertyName) {
        return readProperty(propertyName);
    }

    @Override
    public List<String> getProperties(String[] propertyNames){
        loadProperties();
        List<String> properties = new ArrayList<>();
        for (String propertyName: propertyNames){
            String value = this.properties.getProperty(propertyName);
            if (value != null && value != "") {
                properties.add(value);
            }else{
                log.error("invalid property name #"+ propertyName);
                throw new RuntimeException("please enter valid property name");
            }
        }
        return properties;
    }

    @Override
    public Properties loadProperties(){
        try {
            this.properties.load(PropertyReader.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return this.properties;
    }

    private String readProperty(String propertyName){
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")){
            log.error("invalid property name #"+ propertyName);
            throw new RuntimeException("please enter valid property name");
        }
        loadProperties();
        return this.properties.getProperty(propertyName);
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

    private static String stripLeadingSlash(String resourceName) {
        return (resourceName.startsWith("/")) ? resourceName.substring(1) : resourceName;
    }

    private String removeSymbols(String resourceName){
        String path = Thread.currentThread()
                .getContextClassLoader()
                .getResource(stripLeadingSlash(resourceName))
                .getFile()
                .replaceAll("%20"," ");
        path = path.startsWith("file:\\") ? path.substring(6) : path;
        //path = path.replace("")
        return path;
    }
}
