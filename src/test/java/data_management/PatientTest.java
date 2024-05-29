package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientTest {

    private Patient patient;

    @BeforeEach
    public void setup() {
        patient = new Patient(1);
    }

    @Test
    public void testAddRecord() {
        long now = System.currentTimeMillis();
        patient.addRecord(120.0, "BloodPressure", now);
        List<PatientRecord> records = patient.getRecords();
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals("BloodPressure", records.get(0).getRecordType());
        assertEquals(now, records.get(0).getTimestamp());
    }

    @Test
    public void testGetRecordsWithinTimeFrame() {
        long now = System.currentTimeMillis();
        patient.addRecord(120.0, "BloodPressure", now - 20000);
        patient.addRecord(125.0, "BloodPressure", now - 10000);
        patient.addRecord(130.0, "BloodPressure", now);

        List<PatientRecord> records = patient.getRecords(now - 15000, now + 5000);
        assertEquals(2, records.size());
    }

    @Test
    public void testGetRecords_NoRecordsInTimeFrame() {
        long now = System.currentTimeMillis();
        patient.addRecord(120.0, "BloodPressure", now - 20000);
        patient.addRecord(125.0, "BloodPressure", now - 15000);

        List<PatientRecord> records = patient.getRecords(now - 10000, now);
        assertTrue(records.isEmpty());
    }
}
