package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PhpParserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PhpFileStructure parsePhpCode(String phpCode) throws IOException, InterruptedException {
        // Define the absolute path to the PHP executable
        String phpExecutable = "C:\\php\\php.exe"; // Adjust if PHP is installed elsewhere

        // Define the absolute paths based on your provided hardcoded path
        String phpDir = "C:\\Users\\athar\\Downloads\\phpParser\\demo\\php"; // Working directory
        String parsePhpPath = "C:\\Users\\athar\\Downloads\\phpParser\\demo\\php\\parse.php"; // Full path to parse.php

        // Validate the directory exists
        File phpDirectory = new File(phpDir);
        if (!phpDirectory.exists() || !phpDirectory.isDirectory()) {
            throw new IOException("PHP directory does not exist or is invalid: " + phpDir);
        }

        // Validate the parse.php file exists
        File parsePhpFile = new File(parsePhpPath);
        if (!parsePhpFile.exists() || !parsePhpFile.isFile()) {
            throw new IOException("parse.php file does not exist or is invalid: " + parsePhpPath);
        }

        // Start the PHP process
        ProcessBuilder pb = new ProcessBuilder(phpExecutable, parsePhpPath);
        pb.directory(phpDirectory); // Set working directory to C:\Users\athar\Downloads\phpParser\demo\php

        // Log command for debugging
        System.out.println("Executing command: " + pb.command() + " in directory: " + phpDir);

        Process process = pb.start();

        // Write PHP code to the process's stdin
        try (OutputStream os = process.getOutputStream()) {
            os.write(phpCode.getBytes());
            os.flush();
        }

        // Read the JSON output from the process
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Capture stderr for debugging
        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("PHP process exited with code " + exitCode + ". Error: " + errorOutput.toString());
        }

        // Deserialize JSON to PhpFileStructure
        String jsonOutput = output.toString();
        return objectMapper.readValue(jsonOutput, PhpFileStructure.class);
    }
}