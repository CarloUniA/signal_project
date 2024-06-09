package com.data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketClientTest {

    private static WebSocketOutputStrategy outputStrategy;

    @BeforeAll
    public static void startServer() {
        outputStrategy = new WebSocketOutputStrategy(8080);
        outputStrategy.start();
    }

    @AfterAll
    public static void stopServer() {
        outputStrategy.stop();
    }

    @Test
    public void testConnection() throws URISyntaxException, InterruptedException {
        DataStorage dataStorage = new DataStorage();
        WebSocketClientImpl client = new WebSocketClientImpl(new URI("ws://localhost:8080"), dataStorage);

        client.connectBlocking(5, TimeUnit.SECONDS); // Wait for the connection to be established
        assertTrue(client.isOpen());
    }

    @Test
    public void testHandleData() throws URISyntaxException {
        DataStorage dataStorage = new DataStorage();
        WebSocketClientImpl client = new WebSocketClientImpl(new URI("ws://localhost:8080"), dataStorage);

        String testData = "1,1234567890,HeartRate,70";
        client.handleData(testData, dataStorage);

        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertFalse(records.isEmpty());
        assertEquals(70, records.get(0).getMeasurementValue());
    }
}
