package com.data_management;

import com.alerts.AlertGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Singleton class for managing storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by patient IDs.
 */
public class DataStorageWeek7 {
    private static DataStorage instance;
    private Map<Integer, Patient> patientMap;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the underlying storage structure.
     */
    private DataStorageWeek7() {
        this.patientMap = new HashMap<>();
    }

    /**
     * Provides the global point of access to the single instance of DataStorage.
     *
     * @return the single instance of DataStorage
     */
    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public synchronized void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public synchronized List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>();
    }

    public synchronized List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }
}

