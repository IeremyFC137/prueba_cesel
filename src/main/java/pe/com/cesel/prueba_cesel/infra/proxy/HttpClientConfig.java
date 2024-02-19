package pe.com.cesel.prueba_cesel.infra.proxy;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class HttpClientConfig {

    @Value("${proxy.host:}")
    private String proxyHost;

    @Value("${proxy.port:}")
    private Integer proxyPort;

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig.Builder configBuilder = RequestConfig.custom();

        if (StringUtils.hasText(proxyHost) && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            configBuilder = configBuilder.setProxy(proxy);
        }

        RequestConfig config = configBuilder.build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();
    }
}