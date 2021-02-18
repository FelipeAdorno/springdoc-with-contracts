package br.com.monkey.ecx.apidocumentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.lang.GroovyShell;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import shaded.org.apache.maven.model.Model;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@AllArgsConstructor
public class SpringCloudContractOpenAPICustomiser implements OpenApiCustomiser {

    private static final String MAPPINGS = "/mappings/";

    private static final String TARGET_PATH = "target/stubs/META-INF/";

    public static final String OPERATION_ID_SUFFIX = ".json";

    private final Model model;

    @Override
    public void customise(OpenAPI openApi) {
        openApi.getPaths().forEach((s, pathItem) -> pathItem.readOperations().forEach(operation -> {
            try {
                if (operation.getOperationId().contains(OPERATION_ID_SUFFIX)) {
                    File file = new File(TARGET_PATH + model.getGroupId() + File.separator + model.getArtifactId()
                            + File.separator + model.getVersion() + MAPPINGS + operation.getOperationId());

                    if (file.exists()) {

                        if (nonNull(pathItem.getPost())) {
                            setRequestExample(pathItem.getPost());
                        }

                        if (nonNull(pathItem.getPut())) {
                            setRequestExample(pathItem.getPut());
                        }

                        if (nonNull(pathItem.getPatch())) {
                            setRequestExample(pathItem.getPatch());
                        }

                        setResponseExample(operation, file);

                    } else {
                        throw new IllegalStateException(
                                "Please check the operation id: " + file.getName() + " for API: " + s);
                    }
                }
            } catch (IllegalStateException ex) {
                throw ex;
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }));
    }


    private void setResponseExample(Operation operation, File file) {
        Optional<Contract.Response> response = getResponseFromContract(file);
        if (!isEmpty(operation.getResponses()) && response.isPresent()) {
            operation.getResponses().forEach((statusCode, apiResponse) -> {
                if (Integer.valueOf(statusCode).equals(response.get().getStatus())) {

                    Example example = new Example();
                    String value = response.get().getBody();

                    try {
                        if (StringUtils.hasText(value)) {
                            ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
                            example.setValue(mapper.readTree(value));

                            MediaType mediaType = new MediaType();
                            mediaType.setExample(example.getValue());

                            if (isNull(apiResponse)) {
                                apiResponse = new ApiResponse();
                            }

                            if (isNull(apiResponse.getContent())) {
                                apiResponse.setContent(new Content());
                            }

                            MediaType oldType = apiResponse.getContent().get(ALL_VALUE);
                            mediaType.setSchema(oldType.getSchema());

                            apiResponse.getContent().put(APPLICATION_JSON_VALUE, mediaType);
                            apiResponse.getContent().remove(ALL_VALUE);
                        }
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                }
            });
        }
    }

    private void setRequestExample(Operation operation) {
        try {
            if (nonNull(operation.getRequestBody()) && nonNull(operation.getRequestBody().getContent())) {
                File groovy = new File(TARGET_PATH + model.getGroupId() + File.separator + model.getArtifactId()
                        + File.separator + model.getVersion() + "/contracts/"
                        + operation.getOperationId().replace(".json", ".groovy"));
                org.springframework.cloud.contract.spec.Contract evaluate = (org.springframework.cloud.contract.spec.Contract) new GroovyShell()
                        .evaluate(groovy);
                if (nonNull(evaluate.getRequest()) && nonNull(evaluate.getRequest().getBody())
                        && nonNull(evaluate.getRequest().getBody().getClientValue())
                        && nonNull(operation.getRequestBody().getContent().get(APPLICATION_JSON_VALUE))) {

                    Example example = new Example();
                    String value = (String) evaluate.getRequest().getBody().getClientValue();

                    ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
                    example.setValue(mapper.readTree(value));

                    operation.getRequestBody().getContent().get(APPLICATION_JSON_VALUE).example(example.getValue());
                }

            }
        } catch (IOException e) {
            // not to do
        }
    }

    private Optional<Contract.Response> getResponseFromContract(File file) {
        Optional<Contract.Response> response = Optional.empty();
        try {
            Contract contract = ObjectMapperFactory.buildStrictGenericObjectMapper().readValue(file, Contract.class);
            if (nonNull(contract) && nonNull(contract.getResponse())
                    && contract.getResponse().getStatus() != HttpStatus.NO_CONTENT.value()
                    && nonNull(contract.getResponse().getBody())) {
                response = Optional.of(contract.getResponse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
