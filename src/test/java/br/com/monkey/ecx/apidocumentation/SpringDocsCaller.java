
package br.com.monkey.ecx.apidocumentation;

import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpringDocsCaller {

    private final File outputDir = new File("openapi");

    private static final String GET = "GET";

    public ResponseEntity<?> execute(Integer serverPort) {
        try {
            String apiDocsUrl = "http://localhost:" + serverPort + "/v3/api-docs/";
            URL urlForGetRequest = new URL(apiDocsUrl);
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod(GET);
            int responseCode = conection.getResponseCode();
            String result = this.readFullyAsString(conection.getInputStream());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                outputDir.mkdirs();
                String outputFileName = "openapi";
                Files.write(Paths.get(outputDir + "/" + outputFileName.concat(".json")),
                        result.getBytes(StandardCharsets.UTF_8));
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(responseCode).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private String readFullyAsString(InputStream inputStream) throws IOException {
        return readFully(inputStream).toString(StandardCharsets.UTF_8.name());
    }

    private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }

}