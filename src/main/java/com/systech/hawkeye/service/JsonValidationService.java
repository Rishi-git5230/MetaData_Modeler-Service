package com.systech.hawkeye.service;

import com.systech.hawkeye.schema.CustomSchemaLoader; // Use your custom loader
import com.systech.hawkeye.validation.ValidationException; // Your custom exception
import org.everit.json.schema.Schema;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JsonValidationService {

    private static final Logger logger = LoggerFactory.getLogger(JsonValidationService.class);
    private final CustomSchemaLoader schemaLoader;

    public JsonValidationService() {
        this.schemaLoader = new CustomSchemaLoader(); // Initialize your custom loader
    }

    public void validateJson(String json, String version) throws IOException {
        String schemaPath = "src/main/java/com/systech/hawkeye/implementation/v" + version + "/userSchema.json"; // Adjust path as necessary
        Schema schema;

        try {
            schema = schemaLoader.loadSchema(schemaPath); // Load the schema
        } catch (IOException e) {
            logger.error("Error loading schema from path: {}", schemaPath, e);
            throw new IOException("Failed to load schema: " + e.getMessage(), e);
        }

        JSONObject jsonData = new JSONObject(json);

        try {
            schema.validate(jsonData); // This will throw ValidationException on validation failure
        } catch (ValidationException e) {
            String errorMessage = "Validation failed: " + e.getMessage();
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        // Save the validated JSON
        saveJson(json);
    }

    private void saveJson(String json) throws IOException {
        Path outputPath = Paths.get("validated_jsons");
        if (!Files.exists(outputPath)) {
            Files.createDirectory(outputPath);
        }

        // Use a unique timestamp and a hash or UUID for the file name to avoid conflicts
        String fileName = "validated_" + System.currentTimeMillis() + "_" + java.util.UUID.randomUUID().toString() + ".json";

        // Save the JSON to the specified file
        Files.writeString(outputPath.resolve(fileName), json);
        logger.info("Successfully saved validated JSON to: {}", fileName);
    }
}
