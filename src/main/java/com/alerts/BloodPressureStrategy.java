package com.alerts;

import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(PatientRecord record) {
        // Alert if blood pressure is above 180 or below 90
        return "BloodPressure".equals(record.getRecordType()) && (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90);
    }
}


