package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of a WebSocket client that connects to a WebSocket server to receive real-time data.
 * This class parses incoming messages and stores the parsed information in the DataStorage.
 */
public class WebSocketClientImpl extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Used to wait for the connection to be established

    /**
     * Constructs a WebSocketClientImpl with the specified server URI and data storage.
     *
     * @param serverUri the URI of the WebSocket server
     * @param dataStorage the storage where data will be stored
     */
    public WebSocketClientImpl(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    /**
     * Called when the connection to the WebSocket server is opened.
     *
     * @param handshakedata the handshake data
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server.");
        connectionLatch.countDown(); // Notify that the connection is established
    }

    /**
     * Called when a message is received from the WebSocket server.
     *
     * @param message the received message
     */
    @Override
    public void onMessage(String message) {
        handleData(message, dataStorage);
    }

    /**
     * Called when the connection to the WebSocket server is closed.
     *
     * @param code the closing code
     * @param reason the reason for the closure
     * @param remote whether the closure was initiated by the remote host
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server.");
    }

    /**
     * Called when an error occurs with the WebSocket connection.
     *
     * @param ex the exception that occurred
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Initiates the connection to the WebSocket server and waits for the connection to be established.
     */
    @Override
    public void readData(DataStorage dataStorage) {
        this.connect();
        try {
            connectionLatch.await(5, TimeUnit.SECONDS); // Wait for up to 5 seconds for the connection to be established
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a single data entry and stores it in the data storage.
     *
     * @param data the data entry to handle
     * @param dataStorage the storage where data will be stored
     */
    @Override
    public void handleData(String data, DataStorage dataStorage) {
        String[] parts = data.split(",");
        if (parts.length == 4) {
            try {
                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String label = parts[2];
                double value = Double.parseDouble(parts[3]);
                dataStorage.addPatientData(patientId, value, label, timestamp);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse data: " + data);
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid data format: " + data);
        }
    }
}
