package com.data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class IntegrationTest {

    @Test
    public void testRealTimeDataProcessing() throws URISyntaxException {
        DataStorage dataStorage = new DataStorage();
        WebSocketClientImpl client = new WebSocketClientImpl(new URI("ws://localhost:8080"), dataStorage);

        // Mock real-time data feed
        String testData = "1,1234567890,HeartRate,70";
        client.onMessage(testData);

        // Verify data is processed and stored
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(70, records.get(0).getMeasurementValue());
    }
}
