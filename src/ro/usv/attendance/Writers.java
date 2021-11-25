package ro.usv.attendance;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

public class Writers {
    public void csvWriterAll(List<List<String>> buffer, String path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        for (List<String> strings : buffer) {
            String[] array = strings.toArray(new String[0]);
            writer.writeAll(Collections.singleton(array));
        }
        writer.close();
    }
}
