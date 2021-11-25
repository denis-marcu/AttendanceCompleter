package ro.usv.attendance;

import com.opencsv.CSVReader;
import ro.usv.attendance.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Readers {
    protected static List<List<String>> readFromCsv(String path){
        List<List<String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        records.remove(0);
        return records;
    }

    protected static List<String> readFromCsvWithStreams(String fileName){
        List<String> data = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Paths.get(fileName))){
//            data = stream
//                    .map(FileU)
        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    protected static List<String> readFromSbv(String path){
        List<String> sbvRows = new ArrayList<>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                sbvRows.add(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found.");
        }
        return formatSbv(sbvRows);
    }

    private static List<String> formatSbv(List<String> sbvRows) {
        List<String> filteredSbvRows = new ArrayList<>();
        for (String name : sbvRows) {
            if (name.matches(".*[A-Za-z].*")) {
                filteredSbvRows.add(name.substring(name.lastIndexOf(":") + 1));
            }
        }
        return filteredSbvRows;
    }


}
