package com.alerts;

import com.data_management.PatientRecord;

public interface AlertStrategy {
    boolean checkAlert(PatientRecord record);
}

