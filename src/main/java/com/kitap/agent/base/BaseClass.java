package com.kitap.agent.base;

import com.kitap.agent.util.PropertyReader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public abstract class BaseClass{
    final protected String separator = File.separator;
    final PropertyReader reader = new PropertyReader();
    final public Properties properties = reader.loadProperties();

    protected String getProperty(String propertyName){
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")){
            log.error("invalid property name #"+ propertyName);
            throw new RuntimeException("please enter valid property name");
        }
        return properties.getProperty(propertyName);
    }

    public List<String> getProperties(String[] propertyNames){
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
}
