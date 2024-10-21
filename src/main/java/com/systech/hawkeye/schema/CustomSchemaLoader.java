package com.systech.hawkeye.schema;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader; // Import the Everit SchemaLoader
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomSchemaLoader {

    public Schema loadSchema(String schemaFilePath) throws IOException {
        String schemaContent = new String(Files.readAllBytes(Paths.get(schemaFilePath)));
        JSONObject jsonSchema = new JSONObject(schemaContent);

        SchemaLoader schemaLoader = SchemaLoader.builder()
                .schemaJson(jsonSchema)
                .resolutionScope("file://" + schemaFilePath)
                .build(); // Call build to create the SchemaLoader instance

        return schemaLoader.load().build(); // Use the built SchemaLoader to load
    }

    public void saveJsonToFile(String json, String filePath) throws IOException {
        Files.write(Paths.get(filePath), json.getBytes());
    }
}
