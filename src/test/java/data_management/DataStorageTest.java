package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.AlertGenerator;
import com.data_management.FileDataReader;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class DataStorageTest {

   /* @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        DataStorage storage = new DataStorage(reader);
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }*/
    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }

    // Test for adding and retrieving patient records in DataStorage
    @Test
    void testAddAndGetRecords() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        Patient patient = storage.getAllPatients().stream().filter(p -> p.getPatientId() == 1).findFirst().orElse(null);
        assertNotNull(patient);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
    }

    // Test for reading data from a file using FileDataReader
    @Test
    void testFileDataReader() throws IOException {
        String testFilePath = "test_data.txt";
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("1,98.6,Temperature,1615158000000\n");
            writer.write("1,120.0,HeartRate,1615158001000\n");
            writer.write("2,110.0,HeartRate,1615158002000\n");
        }

        FileDataReader reader = new FileDataReader(testFilePath);
        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertFalse(patients.isEmpty());

        Patient patient1 = storage.getAllPatients().stream().filter(p -> p.getPatientId() == 1).findFirst().orElse(null);
        assertNotNull(patient1);
        assertEquals(2, patient1.getRecords().size());

        Patient patient2 = storage.getAllPatients().stream().filter(p -> p.getPatientId() == 2).findFirst().orElse(null);
        assertNotNull(patient2);
        assertEquals(1, patient2.getRecords().size());
    }

    // Test for generating alerts in AlertGenerator
    @Test
    void testAlertGeneration() {
        storage.addPatientData(1, 190.0, "BloodPressure", System.currentTimeMillis());
        storage.addPatientData(1, 85.0, "OxygenSaturation", System.currentTimeMillis());

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().stream().filter(p -> p.getPatientId() == 1).findFirst().orElse(null);
        assertNotNull(patient);

        alertGenerator.evaluateData(patient);

        // Verify alerts were generated - This part should be extended to check the actual alerts
    }

    // Test for adding and retrieving records within the Patient class
    @Test
    void testAddAndRetrieveRecords() {
        Patient patient = new Patient(1);
        long currentTime = System.currentTimeMillis();
        patient.addRecord(98.6, "Temperature", currentTime - 5000);
        patient.addRecord(120.0, "HeartRate", currentTime - 3000);
        patient.addRecord(110.0, "HeartRate", currentTime - 1000);

        List<PatientRecord> records = patient.getRecords(currentTime - 4000, currentTime);
        assertEquals(2, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals(110.0, records.get(1).getMeasurementValue());
    }

    // Test for retrieving records within a specified time range
    @Test
    void testGetRecordsWithinTimeRange() {
        Patient patient = new Patient(1);
        long currentTime = System.currentTimeMillis();
        patient.addRecord(98.6, "Temperature", currentTime - 10000);
        patient.addRecord(120.0, "HeartRate", currentTime - 5000);
        patient.addRecord(110.0, "HeartRate", currentTime - 3000);
        patient.addRecord(115.0, "HeartRate", currentTime - 1000);

        List<PatientRecord> records = patient.getRecords(currentTime - 6000, currentTime - 2000);
        assertEquals(2, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals(110.0, records.get(1).getMeasurementValue());
    }

    // Test for retrieving records outside a specified time range
    @Test
    void testGetRecordsOutsideTimeRange() {
        Patient patient = new Patient(1);
        long currentTime = System.currentTimeMillis();
        patient.addRecord(98.6, "Temperature", currentTime - 10000);
        patient.addRecord(120.0, "HeartRate", currentTime - 5000);
        patient.addRecord(110.0, "HeartRate", currentTime - 3000);
        patient.addRecord(115.0, "HeartRate", currentTime - 1000);

        List<PatientRecord> records = patient.getRecords(currentTime - 2000, currentTime);
        assertEquals(1, records.size());
        assertEquals(115.0, records.get(0).getMeasurementValue());
    }
}
