package data_management;


import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataReaderTest {

    private DataStorage dataStorage;

    @BeforeEach
    public void setup() {
        dataStorage = new DataStorage();
    }

    @Test
    public void testFileDataReader() throws IOException {
        // Create a temporary test data file
        String testData = "1,120.0,BloodPressure,1627849920\n2,95.0,OxygenSaturation,1627849930\n3,80.0,HeartRate,1627849940";
        Path path = Files.createTempFile("test_data", ".csv");
        Files.write(path, testData.getBytes());

        FileDataReader reader = new FileDataReader(path.toString());
        reader.readData(dataStorage);

        assertEquals(3, dataStorage.getAllPatients().size());
        assertEquals(1, dataStorage.getRecords(1, 0, Long.MAX_VALUE).size());
        assertEquals(1, dataStorage.getRecords(2, 0, Long.MAX_VALUE).size());
        assertEquals(1, dataStorage.getRecords(3, 0, Long.MAX_VALUE).size());
    }
}


