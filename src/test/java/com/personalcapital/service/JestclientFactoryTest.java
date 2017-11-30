package com.personalcapital.service;

import com.personalcapital.service.client.ElasticSearchClient;
import io.searchbox.client.JestClient;
import org.junit.Test;

public class JestclientFactoryTest {

    @Test
    public void clientCreationWithTimeout() {
        JestClient client = ElasticSearchClient.getInstance().getJestClient();
        assert client != null;
    }
}

