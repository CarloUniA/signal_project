package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.ArrayList;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
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
        checkBloodPressureAlerts(patient, records);
        checkOxygenSaturationAlerts(patient, records);
        checkCombinedAlert(patient, records);
        checkECGAlerts(patient, records);
    }

    /**
     * Checks blood pressure records to trigger alerts based on predefined criteria.
     *
     * @param patient the patient data to evaluate for alert conditions
     * @param records the list of patient records
     */
    private void checkBloodPressureAlerts(Patient patient, List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            String recordType = record.getRecordType();
            double value = record.getMeasurementValue();
            long timestamp = record.getTimestamp();

            if ("BloodPressure".equals(recordType)) {
                if (value > 180 || value < 90) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Blood Pressure", timestamp));
                }
                if (i >= 2) {
                    double prevValue1 = records.get(i - 1).getMeasurementValue();
                    double prevValue2 = records.get(i - 2).getMeasurementValue();
                    if (value > prevValue1 && prevValue1 > prevValue2 && (value - prevValue1) > 10 && (prevValue1 - prevValue2) > 10) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Increasing Blood Pressure Trend", timestamp));
                    } else if (value < prevValue1 && prevValue1 < prevValue2 && (prevValue1 - value) > 10 && (prevValue2 - prevValue1) > 10) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Decreasing Blood Pressure Trend", timestamp));
                    }
                }
            }
        }
    }

    /**
     * Checks oxygen saturation records to trigger alerts based on predefined criteria.
     *
     * @param patient the patient data to evaluate for alert conditions
     * @param records the list of patient records
     */
    private void checkOxygenSaturationAlerts(Patient patient, List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            String recordType = record.getRecordType();
            double value = record.getMeasurementValue();
            long timestamp = record.getTimestamp();

            if ("OxygenSaturation".equals(recordType)) {
                if (value < 92) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low Oxygen Saturation", timestamp));
                }
                if (i > 0) {
                    double prevValue = records.get(i - 1).getMeasurementValue();
                    long prevTimestamp = records.get(i - 1).getTimestamp();
                    if ((prevValue - value) >= 5 && (timestamp - prevTimestamp) <= 600000) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Rapid Oxygen Saturation Drop", timestamp));
                    }
                }
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
     * Checks ECG records to trigger alerts for abnormal data.
     *
     * @param patient the patient data to evaluate for alert conditions
     * @param records the list of patient records
     */
    private void checkECGAlerts(Patient patient, List<PatientRecord> records) {
        double sum = 0;
        int count = 0;
        List<Double> values = new ArrayList<>();

        for (PatientRecord record : records) {
            if ("ECG".equals(record.getRecordType())) {
                double value = record.getMeasurementValue();
                sum += value;
                values.add(value);
                count++;

                if (count >= 10) { // Using a window of 10 readings
                    double average = sum / count;
                    for (double v : values) {
                        if (v > average * 1.5) {
                            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Abnormal ECG Data", record.getTimestamp()));
                            break;
                        }
                    }
                    sum -= values.remove(0);
                    count--;
                }
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