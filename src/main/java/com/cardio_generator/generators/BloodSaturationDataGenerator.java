package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * Generates simulated blood saturation data for patients. This generator creates random,
 * small fluctuations around a baseline saturation value to simulate realistic monitoring conditions.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a BloodSaturationDataGenerator with a specific number of patients.
     * Initializes baseline saturation values for each patient between 95 and 100 percent.
     *
     * @param patientCount the number of patients to generate data for
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation reading for a specified patient and sends it to an output strategy.
     * The method simulates small random fluctuations in saturation and ensures values stay within a healthy range.
     *
     * @param patientId the ID of the patient for whom data is being generated
     * @param outputStrategy the strategy used to output the data
     * @throws RuntimeException if an error occurs in generating or sending the data. This exception is
     * captured and logged within the method, and does not terminate the method prematurely.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}