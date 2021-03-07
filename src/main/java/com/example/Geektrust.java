package com.example;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Geektrust {
    public static void main(String[] args) {
        try {
            String pathToFile = args[0];
            List<String> testCases = Files.readAllLines(Paths.get(pathToFile), StandardCharsets.UTF_8);
            Family family =  new Family();

            for (String testCase : testCases) {
                family.handleTest(testCase);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
