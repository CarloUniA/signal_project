package com.alerts;

import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(PatientRecord record) {
        // Add logic for checking heart rate alert
        return record.getMeasurementValue() > 100 || record.getMeasurementValue() < 60; // Example condition
    }
}


