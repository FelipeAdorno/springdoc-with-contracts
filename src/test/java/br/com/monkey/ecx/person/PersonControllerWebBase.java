package br.com.monkey.ecx.person;

import br.com.monkey.ecx.SpringdocPocApplication;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

@SpringBootTest(classes = SpringdocPocApplication.class)
public class PersonControllerWebBase {

    @BeforeAll
    static void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders
                .standaloneSetup(new PersonController());
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }
}