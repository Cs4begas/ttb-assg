package ttb.assg.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonFileReader {

    @Autowired
    private ObjectMapper objectMapper;

    public String readJson(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes());
        }
    }

    public <T> T readJsonFileToObject(String filePath, Class<T> objectType) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, objectType);
        }
    }
}