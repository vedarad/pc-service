package com.personalcapital.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private Properties properties = new Properties();

    /**
     * Read all properties from file
     * @return Properties
     */
    private Properties readProperties() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("Exception occurred while reading properties file" + e.getMessage());
                }
            }
        }catch (Exception e){
            log.info("Generic exception {}", e.getStackTrace());
        }
        return properties;
    }

    /**
     * Returns HashMap of AWS configs from properties file
     * @return Map of AWS Configs
     */
    public Map<String, String> getCredentials() {
        if(properties.size() == 0)
            getProperties();

        Map<String, String> awsConfig = new HashMap<>();
        awsConfig.put("accessKey", properties.getProperty("accessKey"));
        awsConfig.put("secretKey", properties.getProperty("secretKey"));
        return awsConfig;
    }

    /**
     * Returns HashMap of elastic configs from properties file
     * @return Map of ElasticSearch Configs
     */
    public Map<String, String> getElasticConfig() {
        if(properties.size() == 0)
            getProperties();

        Map<String, String> elasticConfig = new HashMap<>();
        elasticConfig.put("elasticSearchEndpoint", properties.getProperty("elasticSearchEndpoint"));
        elasticConfig.put("searchIndexAlias", properties.getProperty("searchIndexAlias"));
        elasticConfig.put("elasticSearchPort", properties.getProperty("elasticSearchPort"));
        elasticConfig.put("searchType", properties.getProperty("searchType"));
        return elasticConfig;
    }

    /**
     * Returns HashMap of elastic configs from properties file
     * @return Map of ElasticSearch Configs
     */
    public Map<String, String> getFields() {
        if(properties.size() == 0)
            getProperties();

        Map<String, String> fields = new HashMap<>();
        fields.put("planFieldName", properties.getProperty("planFieldName"));
        fields.put("sponsorFieldName", properties.getProperty("sponsorFieldName"));
        fields.put("usState", properties.getProperty("usState"));
        fields.put("id", properties.getProperty("id"));
        return fields;
    }

    /**
     * Returns properties read from properties file
     * @return Properties
     */
    private Properties getProperties() {
        return readProperties();
    }
}
