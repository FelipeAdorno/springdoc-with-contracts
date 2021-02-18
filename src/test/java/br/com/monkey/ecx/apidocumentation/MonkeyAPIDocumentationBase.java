package br.com.monkey.ecx.apidocumentation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ComponentScan(basePackageClasses = MonkeyAPIDocumentationConfiguration.class)
@TestPropertySource(properties = {"springdoc.api-docs.groups.enabled=true", "spring.flyway.enabled=false",
        "spring.main.banner-mode=off", "logging.level.root=ERROR"})
public class MonkeyAPIDocumentationBase {

    @LocalServerPort
    private int randomServerPort;

    @Test
    void generateDocs() {
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");
        System.err.println("TA EXCEUTANDO O TESTE");

        assertEquals(HttpStatus.OK, new SpringDocsCaller().execute(randomServerPort).getStatusCode(),
                "Error on generate Docs for API.");
    }

}
