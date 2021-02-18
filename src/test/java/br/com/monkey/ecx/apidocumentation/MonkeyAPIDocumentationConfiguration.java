package br.com.monkey.ecx.apidocumentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shaded.org.apache.maven.model.Model;
import shaded.org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.io.IOException;

import static java.util.Objects.isNull;

@Configuration
public class MonkeyAPIDocumentationConfiguration {

    private final Model appDetails = getAppDetails();

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components())
                .info(new Info().title(appDetails.getName()).version(appDetails.getVersion()));
    }

    @Bean
    public OpenApiCustomiser springCloudContractCustomizer() {
        return new SpringCloudContractOpenAPICustomiser(appDetails);
    }

    public Model getAppDetails() {
        if (isNull(appDetails)) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                return reader.read(new FileReader("pom.xml"));
            } catch (IOException | XmlPullParserException e) {
                throw new IllegalStateException("Error on load APP details.");
            }
        } else {
            return appDetails;
        }
    }

}