package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.user}")
    String username;

    @Value("${rest.template.password}")
    String password;

    @Value("${rest.template.rootUrl}")
    String rootUrl;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        RestTemplateBuilder restTemplateBuilder = configurer.configure(new RestTemplateBuilder());
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(rootUrl);

        RestTemplateBuilder authBuilder = restTemplateBuilder.basicAuthentication(username, password);

        return authBuilder.uriTemplateHandler(uriBuilderFactory);
    }
}
