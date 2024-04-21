package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the OutputStrategy to output data to files. This strategy creates a file for each type of data label
 * and writes the data into the corresponding file in a specified base directory.
 */
public class FileOutputStrategy implements OutputStrategy {

    // The directory where data files are stored.
    private String baseDirectory;

    // Maps data labels to their respective file paths for efficient data output.
    public static ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Initializes a new FileOutputStrategy with a specified base directory for storing files.
     *
     * @param baseDirectory the directory where data files will be saved.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs the specified data to a file named by the label within the base directory.
     * If the directory does not exist, it is created. Each piece of data is appended to the file corresponding to its label.
     *
     * @param patientId the unique identifier for the patient whose data is being output.
     * @param timestamp the timestamp at which the data was generated.
     * @param label the label indicating the type of data, which determines the file name.
     * @param data the actual data to be written to the file.
     * @throws IOException if an error occurs while creating the directory or writing to the file. Although this exception
     * is caught and handled within the method, it is documented here to alert users of potential issues during file operations.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
