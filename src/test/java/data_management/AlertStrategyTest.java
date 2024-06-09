package com.alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

public class AlertStrategyTest {

    @Test
    public void testBloodPressureStrategy() {
        AlertStrategy strategy = new BloodPressureStrategy();

        // Test for high blood pressure
        PatientRecord highBpRecord = new PatientRecord(1, 190, "BloodPressure", System.currentTimeMillis());
        assertTrue(strategy.checkAlert(highBpRecord), "Expected alert for high blood pressure");

        // Test for low blood pressure
        PatientRecord lowBpRecord = new PatientRecord(1, 80, "BloodPressure", System.currentTimeMillis());
        assertTrue(strategy.checkAlert(lowBpRecord), "Expected alert for low blood pressure");

        // Test for normal blood pressure
        PatientRecord normalBpRecord = new PatientRecord(1, 120, "BloodPressure", System.currentTimeMillis());
        assertFalse(strategy.checkAlert(normalBpRecord), "Expected no alert for normal blood pressure");

        // Test for other measurement type
        PatientRecord otherRecord = new PatientRecord(1, 120, "HeartRate", System.currentTimeMillis());
        assertFalse(strategy.checkAlert(otherRecord), "Expected no alert for non-blood pressure record");
    }

    @Test
    public void testHeartRateStrategy() {
        AlertStrategy strategy = new HeartRateStrategy();
        PatientRecord record = new PatientRecord(1, 110, "HeartRate", System.currentTimeMillis());
        assertTrue(strategy.checkAlert(record));
    }

    @Test
    public void testOxygenSaturationStrategy() {
        AlertStrategy strategy = new OxygenSaturationStrategy();
        PatientRecord record = new PatientRecord(1, 85, "OxygenSaturation", System.currentTimeMillis());
        assertTrue(strategy.checkAlert(record));
    }
}


