package com.alerts;

import com.data_management.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(PatientRecord record) {
        return record.getMeasurementValue() < 92;
    }
}


