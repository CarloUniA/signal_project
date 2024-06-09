package com.alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AlertFactoryTest {

    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("123", "High BP", System.currentTimeMillis());
        assertEquals("Blood Pressure Alert: High BP", alert.getCondition());
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("123", "Low Oxygen", System.currentTimeMillis());
        assertEquals("Blood Oxygen Alert: Low Oxygen", alert.getCondition());
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("123", "Irregular ECG", System.currentTimeMillis());
        assertEquals("ECG Alert: Irregular ECG", alert.getCondition());
    }
}

