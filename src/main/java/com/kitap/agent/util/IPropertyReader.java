package com.kitap.agent.util;


import java.util.List;
import java.util.Properties;

public interface IPropertyReader {
    String getProperty(String propertyName);

    List<String> getProperties(String[] propertyNames);

    Properties loadProperties();
}
