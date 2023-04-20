package com.kitap.agent.base;

import com.kitap.agent.util.PropertyReader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Abstract base class to get properties
 * @author KT1450
 */
@Slf4j
public abstract class BaseClass {
    final protected String separator = File.separator;
    private final PropertyReader reader = new PropertyReader();
    public final Properties properties = reader.loadProperties();

    /**
     * Getting property value by using propertyName as input
     * @param propertyName name of the property
     * @return value of the property
     */
    protected String getProperty(String propertyName){
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")){
            log.error("invalid property name #"+ propertyName);
            throw new RuntimeException("please enter valid property name");
        }
        return properties.getProperty(propertyName);
    }

    /**
     * Getting property values by using propertyNames as input
     * @param propertyNames array of names of the properties
     * @return list of property values
     */
    public List<String> getProperties(String[] propertyNames){
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
        return properties;
    }
}