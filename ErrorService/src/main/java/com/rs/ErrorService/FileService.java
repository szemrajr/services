package com.rs.ErrorService;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService {
    public static final String FILE_NAME = "/home/logs/errors.log";

    public void writeToFile(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.append(line);
            writer.append("\n");
        }catch (IOException e){}
    }

    public List<String> readFromFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            return reader.lines().collect(Collectors.toList());
        }catch (IOException e){
            return Collections.singletonList("Error occurred, logs cannot be read");
        }
    }
}
