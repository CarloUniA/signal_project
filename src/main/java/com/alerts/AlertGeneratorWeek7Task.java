package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGeneratorWeek7Task {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGeneratorWeek7Task(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the {@link #triggerAlert}
     * method.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords();
        checkAlerts(new BloodPressureStrategy(), new BloodPressureAlertFactory(), patient, records);
        checkAlerts(new OxygenSaturationStrategy(), new BloodOxygenAlertFactory(), patient, records);
        checkCombinedAlert(patient, records);
        checkAlerts(new HeartRateStrategy(), new ECGAlertFactory(), patient, records);
    }

    /**
     * Checks records to trigger alerts based on predefined criteria using the provided strategy and factory.
     *
     * @param strategy the strategy to determine if an alert should be triggered
     * @param factory the factory to create the alert
     * @param patient the patient data to evaluate for alert conditions
     * @param records the list of patient records
     */
    private void checkAlerts(AlertStrategy strategy, AlertFactory factory, Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (strategy.checkAlert(record)) {
                triggerAlert(factory.createAlert(String.valueOf(patient.getPatientId()), record.getRecordType(), record.getTimestamp()));
            }
        }
    }

    /**
     * Checks for combined alert conditions such as Hypotensive Hypoxemia.
     *
     * @param patient the patient data to evaluate for alert conditions
     * @param records the list of patient records
     */
    private void checkCombinedAlert(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if ("BloodPressure".equals(record.getRecordType()) && record.getMeasurementValue() < 90) {
                for (PatientRecord otherRecord : records) {
                    if ("OxygenSaturation".equals(otherRecord.getRecordType()) && otherRecord.getMeasurementValue() < 92) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia", record.getTimestamp()));
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("ALERT: Patient ID: " + alert.getPatientId() + ", Condition: " + alert.getCondition() + ", Timestamp: " + alert.getTimestamp());
    }

    /**
     * Triggers a manually generated alert, typically by a nurse or patient.
     *
     * @param patientId the ID of the patient for whom the alert is triggered
     * @param condition the condition for the alert
     */
    public void triggerManualAlert(int patientId, String condition) {
        long timestamp = System.currentTimeMillis();
        triggerAlert(new Alert(String.valueOf(patientId), condition, timestamp));
    }
}

