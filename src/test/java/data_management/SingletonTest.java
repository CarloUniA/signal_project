package com.data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.cardio_generator.HealthDataSimulator;
import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    public void testDataStorageSingleton() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testHealthDataSimulatorSingleton() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();
        assertSame(instance1, instance2);
    }
}

