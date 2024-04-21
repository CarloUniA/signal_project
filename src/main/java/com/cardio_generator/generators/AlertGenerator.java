package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert conditions for patients based on random probabilities. This class simulates alerts
 * that are either triggered or resolved, depending on the current state and random chance.
 *
 * It implements the {@link PatientDataGenerator} interface for integration with a system
 * that requires dynamic alert management for patient monitoring.
 */
public class AlertGenerator implements PatientDataGenerator {

    // Generates random numbers, used to determine whether to trigger or resolve alerts
    private static final Random RANDOM_GENERATOR = new Random();

    // Array to track alert states for patients; true if an alert is active, false otherwise
    private boolean[] alertStates; // tracks whether an alert is active (true) or resolved (false)

    /**
     * Constructs an AlertGenerator for a specified number of patients.
     * Initializes all patients' alert states to false (no alert active initially).
     *
     * @param patientCount the number of patients to monitor for alerts.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1]; // Index 0 is unused for easier access by patient ID
    }

    /**
     * Generates and outputs an alert status for a given patient. The method randomly decides to trigger or
     * resolve alerts based on predefined probabilities.
     *
     * @param patientId the unique identifier of the patient for whom to generate an alert status.
     * @param outputStrategy the strategy used to output the alert status.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        if (alertStates[patientId]) {
            // Resolve the alert with a high probability
            if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                alertStates[patientId] = false;
                outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
            }
        } else {
            // Trigger a new alert with a small probability
            double lambda = 0.1; // Average rate (alerts per period)
            double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
            boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

            if (alertTriggered) {
                alertStates[patientId] = true;
                outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
            }
        }
    }
}
