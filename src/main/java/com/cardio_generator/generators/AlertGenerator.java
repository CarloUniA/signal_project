package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {

    // Ensured that static final fields use UPPER_SNAKE_CASE as per Google Style Guide
    private static final Random RANDOM_GENERATOR = new Random();

    // Changed array name from "AlertStates" to "alertStates" to follow camelCase convention
    private boolean[] alertStates; // tracks whether an alert is active (true) or resolved (false)

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    @Override
    // Parameter names should be camelCase
    public void generate(int patientId, OutputStrategy outputStrategy) {
        if (alertStates[patientId]) {
            // Method call should follow the field naming convention
            if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                alertStates[patientId] = false;
                outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
            }
        } else {
            // Local variables in methods should be camelCase
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

