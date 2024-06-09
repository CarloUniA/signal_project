package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Output strategy that sets up a WebSocket server to broadcast messages to all connected clients in real-time.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Constructs a WebSocketOutputStrategy with the specified port.
     *
     * @param port the port on which the WebSocket server will listen
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
    }

    /**
     * Starts the WebSocket server.
     */
    public void start() {
        server.start();
    }

    /**
     * Stops the WebSocket server.
     */
    public void stop() {
        try {
            server.stop();
        } catch (InterruptedException e) {
            System.err.println("Failed to stop WebSocket server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param patientId the unique identifier of the patient
     * @param timestamp the time at which the measurement was taken, in milliseconds since the Unix epoch
     * @param label the type of record, e.g., "HeartRate", "BloodPressure"
     * @param data the measurement value to store in the record
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * Implementation of a simple WebSocket server.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        /**
         * Constructs a SimpleWebSocketServer with the specified address.
         *
         * @param address the address on which the server will listen
         */
        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
