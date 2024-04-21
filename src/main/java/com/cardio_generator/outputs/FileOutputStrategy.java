package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name from "BASE_DIRECTORY" to "baseDirectory" to comply with camelCase convention
    private String baseDirectory;

    // Static variable names should be in UPPER_SNAKE_CASE, but since it's not a constant, kept camelCase
    public static ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    // Constructor parameters should be camelCase
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    // Method parameters should be in camelCase
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Use camelCase for method variables
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Local variables should be camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
