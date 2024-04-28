package com.cloud.gateway.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


@Configuration
public class WebClientConfiguration {

    @Value("${webclient.connectionTimeout:5000}")
    private int connectionTimeout; // Default connection timeout is 5 seconds

    @Value("${webclient.readTimeout:5000}")
    private int readTimeout; // Default read timeout is 5 seconds

    @Value("${webclient.pool.maxConnections:200}")
    private int maxConnections; // Maximum number of connections in the pool

    @Bean
    public WebClient.Builder webClientBuilder() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        ConnectionProvider connectionProvider = ConnectionProvider.builder("webClientConnectionPool")
                .maxConnections(maxConnections)
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .compress(true) // Enable compression for better bandwidth utilization
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout))); // Setting read timeout

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(connector);
    }
}
