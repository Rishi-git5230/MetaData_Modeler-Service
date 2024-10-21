package com.systech.hawkeye.controller;

import com.systech.hawkeye.service.JsonValidationService;
import com.systech.hawkeye.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/validate")
public class JsonValidationController {
    private final JsonValidationService jsonValidationService;

    public JsonValidationController(JsonValidationService jsonValidationService) {
        this.jsonValidationService = jsonValidationService;
    }

    @PostMapping("/{version}")
    public ResponseEntity<String> validateJson(@RequestBody String json, @PathVariable String version) {
        try {
            jsonValidationService.validateJson(json, version);
            return ResponseEntity.ok("JSON is valid and has been saved successfully.");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the schema or saving the JSON.");
        }
    }
}
