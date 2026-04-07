package io.github.dutianze.springbootthymeleafreact.shared.config;

import java.util.List;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class GlobalRestClientConfig {

  @Bean
  RestClient.Builder restClientBuilder(
      ClientHttpRequestFactory requestFactory) {
    return RestClient.builder()
        .requestFactory(requestFactory)
        .defaultHeaders(h -> h.setAccept(List.of(MediaType.APPLICATION_JSON)));
  }

  @Bean
  public ClientHttpRequestFactory defaultRequestFactory() {
    return ClientHttpRequestFactoryBuilder.httpComponents()
        .withHttpClientCustomizer(httpClient -> {
          PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
          cm.setMaxTotal(300);
          cm.setDefaultMaxPerRoute(100);
          httpClient.setConnectionManager(cm);
        })
        .build();
  }
}
