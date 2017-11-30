package com.personalcapital.service.client;

import com.personalcapital.service.utils.ConfigLoader;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

public class ElasticSearchClient {

    private static ElasticSearchClient instance = null;
    private static JestClient jestClient = null;
    private ConfigLoader configLoader = new ConfigLoader();
    private String SERVER_URI = configLoader.getElasticConfig().get("elasticSearchEndpoint");

    /**
     * Constructor
     */
    private ElasticSearchClient() { }

    /**
     * Returns an instance of ElasticSearchClient
     * @return ElasticSearchClient
     */
    public static synchronized ElasticSearchClient getInstance() {
        if (instance == null) {
            instance = new ElasticSearchClient();
        }
        return instance;
    }

    /**
     * Returns Jest Client. Rest client for Elastic search
     * Client uses server uri from properties
     * @return Jest Client
     */
    public JestClient getJestClient() {
        if(jestClient == null){
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(SERVER_URI)
                    .defaultCredentials(configLoader.getCredentials().get("accessKey"), configLoader.getCredentials().get("secretKey"))
                    .connTimeout(300000).readTimeout(300000).multiThreaded(true).build());
            jestClient = factory.getObject();
        }
        return jestClient;
    }
}

