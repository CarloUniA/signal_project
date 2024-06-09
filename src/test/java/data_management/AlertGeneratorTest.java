package data_management;

import com.alerts.AlertGeneratorWeek7Task;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGeneratorWeek7Task alertGenerator;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setup() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGeneratorWeek7Task(dataStorage);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testEvaluateData_CriticalBloodPressure() {
        Patient patient = new Patient(1);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(200, "BloodPressure", timestamp);
        dataStorage.addPatientData(1, 200, "BloodPressure", timestamp);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Critical Blood Pressure"));
    }

    @Test
    public void testEvaluateData_LowOxygenSaturation() {
        Patient patient = new Patient(2);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(85, "OxygenSaturation", timestamp);
        dataStorage.addPatientData(2, 85, "OxygenSaturation", timestamp);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Low Oxygen Saturation"));
    }

    @Test
    public void testEvaluateData_IncreasingBloodPressureTrend() {
        Patient patient = new Patient(3);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(100, "BloodPressure", timestamp);
        patient.addRecord(111, "BloodPressure", timestamp + 1000);
        patient.addRecord(122, "BloodPressure", timestamp + 2000);
        dataStorage.addPatientData(3, 100, "BloodPressure", timestamp);
        dataStorage.addPatientData(3, 111, "BloodPressure", timestamp + 1000);
        dataStorage.addPatientData(3, 122, "BloodPressure", timestamp + 2000);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Increasing Blood Pressure Trend"));
    }

    @Test
    public void testEvaluateData_DecreasingBloodPressureTrend() {
        Patient patient = new Patient(4);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(140, "BloodPressure", timestamp);
        patient.addRecord(129, "BloodPressure", timestamp + 1000);
        patient.addRecord(118, "BloodPressure", timestamp + 2000);
        dataStorage.addPatientData(4, 140, "BloodPressure", timestamp);
        dataStorage.addPatientData(4, 129, "BloodPressure", timestamp + 1000);
        dataStorage.addPatientData(4, 118, "BloodPressure", timestamp + 2000);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Decreasing Blood Pressure Trend"));
    }

    @Test
    public void testEvaluateData_RapidOxygenSaturationDrop() {
        Patient patient = new Patient(5);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(97, "OxygenSaturation", timestamp);
        patient.addRecord(91, "OxygenSaturation", timestamp + 600000);
        dataStorage.addPatientData(5, 97, "OxygenSaturation", timestamp);
        dataStorage.addPatientData(5, 91, "OxygenSaturation", timestamp + 600000);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Rapid Oxygen Saturation Drop"));
    }

    @Test
    public void testEvaluateData_HypotensiveHypoxemia() {
        Patient patient = new Patient(6);
        long timestamp = System.currentTimeMillis();
        patient.addRecord(85, "BloodPressure", timestamp);
        patient.addRecord(91, "OxygenSaturation", timestamp + 1000);
        dataStorage.addPatientData(6, 85, "BloodPressure", timestamp);
        dataStorage.addPatientData(6, 91, "OxygenSaturation", timestamp + 1000);

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Hypotensive Hypoxemia"));
    }

    @Test
    public void testEvaluateData_AbnormalECGData() {
        Patient patient = new Patient(7);
        long timestamp = System.currentTimeMillis();
        double[] ecgValues = {0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 2.5};

        for (double value : ecgValues) {
            patient.addRecord(value, "ECG", timestamp);
            timestamp += 1000;
        }

        for (double value : ecgValues) {
            dataStorage.addPatientData(7, value, "ECG", timestamp);
            timestamp += 1000;
        }

        alertGenerator.evaluateData(patient);

        assertTrue(outContent.toString().contains("Abnormal ECG Data"));
    }

    @Test
    public void testTriggerManualAlert() {
        int patientId = 8;
        String condition = "Manual Alert Test";

        alertGenerator.triggerManualAlert(patientId, condition);

        assertTrue(outContent.toString().contains("Manual Alert Test"));
    }
}
