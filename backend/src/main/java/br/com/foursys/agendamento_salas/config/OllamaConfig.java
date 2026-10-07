package br.com.foursys.agendamento_salas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaConfig {

    @Bean
    public RestClient ollamaRestClient(
            @Value("${ollama.url}") String ollamaUrl
    ) {
        return RestClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }
}
