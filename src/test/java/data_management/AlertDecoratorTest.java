package com.alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AlertDecoratorTest {

    @Test
    public void testRepeatedAlertDecorator() {
        Alert alert = new Alert("123", "ECG Alert", System.currentTimeMillis());
        AlertDecorator repeatedAlert = new RepeatedAlertDecorator(alert);
        assertEquals("Repeated ECG Alert", repeatedAlert.getCondition());
    }

    @Test
    public void testPriorityAlertDecorator() {
        Alert alert = new Alert("123", "Blood Pressure Alert", System.currentTimeMillis());
        AlertDecorator priorityAlert = new PriorityAlertDecorator(alert);
        assertEquals("Priority Blood Pressure Alert", priorityAlert.getCondition());
    }
}

