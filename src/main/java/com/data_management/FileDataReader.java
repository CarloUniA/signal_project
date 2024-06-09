package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The {@code FileDataReader} class implements the {@code DataReader} interface
 * and is responsible for reading patient data from a file and storing it into
 * a {@link DataStorage} instance.
 */
public class FileDataReader implements DataReader {
    private String filePath;

    /**
     * Constructs a {@code FileDataReader} with the specified file path.
     *
     * @param filePath the path to the file containing patient data
     */
    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads data from the file and adds it to the specified {@link DataStorage}.
     *
     * @param dataStorage the data storage system that will store the patient data
     * @throws IOException if an I/O error occurs reading from the file
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0]);
                    double measurementValue = Double.parseDouble(parts[1]);
                    String recordType = parts[2];
                    long timestamp = Long.parseLong(parts[3]);
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Parses a line of data and adds it to the specified {@link DataStorage}.
     *
     * @param data the line of data to be parsed and added
     * @param dataStorage the data storage system that will store the patient data
     */
    @Override
    public void handleData(String data, DataStorage dataStorage) {
        String[] parts = data.split(",");
        if (parts.length == 4) {
            int patientId = Integer.parseInt(parts[0]);
            double measurementValue = Double.parseDouble(parts[1]);
            String recordType = parts[2];
            long timestamp = Long.parseLong(parts[3]);
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        }
    }
}
