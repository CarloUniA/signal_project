package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Handles a single data entry and stores it in the data storage.
     *
     * @param data the data entry to handle
     * @param dataStorage the storage where data will be stored
     */
    void handleData(String data, DataStorage dataStorage);
}

