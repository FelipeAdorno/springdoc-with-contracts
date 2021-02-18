package br.com.monkey.ecx;

import br.com.monkey.ecx.apidocumentation.MonkeyAPIDocumentationBase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SpringdocPocApplicationTests extends MonkeyAPIDocumentationBase {

}
