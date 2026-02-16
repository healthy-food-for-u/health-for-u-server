package com.healthforu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.healthforu")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris:http://localhost:9200}")
    private String esUri;

    @Override
    public ClientConfiguration clientConfiguration() {
        // http:// 또는 https:// 가 포함되어 있다면 제거
        String hostAndPort = esUri.replace("http://", "").replace("https://", "");

        return ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();
    }
}
