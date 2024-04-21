package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
/**
 * Implements the OutputStrategy interface to send patient data over a TCP connection.
 * This strategy sets up a TCP server on a specified port and sends data to a connected client.
 *
 * It handles the creation of a server socket, awaits client connections, and then sends data
 * formatted as a string to the client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Initializes a TCP server on the given port and prepares to accept client connections.
     * Once a client is connected, initializes a PrintWriter to send data over the socket.
     *
     * @param port the TCP port where the server will listen for incoming client connections.
     * @throws IOException if an error occurs when creating the server socket or when accepting a client connection.
     *                     This exception is caught and logged, and does not terminate the method execution.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the provided data to the connected client through the TCP connection.
     * The data is sent as a formatted string using the format: "patientId,timestamp,label,data".
     *
     * @param patientId the unique identifier of the patient whose data is being sent.
     * @param timestamp the time at which the data was recorded, in milliseconds since the epoch.
     * @param label a descriptor of the type of data being sent (e.g., "heart rate").
     * @param data the actual data string to be sent to the client.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}