package com.kitap.agent.util;

import java.util.List;
import java.util.Properties;

/**
 * Interface to get properties and load them
 * @author KT1450
 */
public interface IPropertyReader {
    String getProperty(String propertyName);

    List<String> getProperties(String[] propertyNames);

    Properties loadProperties();
}
