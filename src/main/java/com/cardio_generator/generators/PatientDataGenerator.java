package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This interface defines the contract for generating patient data within the cardio_generator system.
 * Implementations of this interface are expected to produce specific types of health data for a given patient,
 * outputting it through an instance of {@link OutputStrategy}.
 *
 * Implementers can produce various types of data like ECG, blood pressure, or blood saturation levels.
 */
public interface PatientDataGenerator {

    /**
     * Generates health data for a specific patient and outputs it using the provided output strategy.
     * The type of data generated is determined by the implementing class.
     *
     * @param patientId the unique identifier for the patient for whom data is to be generated.
     *                  This should be a positive integer representing the patient in a database or a system list.
     * @param outputStrategy the strategy used to output the generated data. This could be console output,
     *                       file output, or another custom strategy implemented by classes that conform to the
     *                       {@link OutputStrategy} interface.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}

