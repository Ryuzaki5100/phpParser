package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PhpParserService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PhpFileStructure parsePhpCode(String phpCode) throws IOException, InterruptedException {
        String phpExecutable = "php";
        String phpDir = "/app/php";
        String parsePhpPath = phpDir + "/parse.php";

        File phpDirectory = new File(phpDir);
        if (!phpDirectory.exists()) {
            throw new IOException("PHP directory does not exist: " + phpDir);
        }

        File parsePhpFile = new File(parsePhpPath);
        if (!parsePhpFile.exists()) {
            throw new IOException("parse.php not found: " + parsePhpPath);
        }

        ProcessBuilder pb = new ProcessBuilder(phpExecutable, parsePhpPath);
        pb.directory(phpDirectory);

        Process process = pb.start();

        try (OutputStream os = process.getOutputStream()) {
            os.write(phpCode.getBytes());
            os.flush();
        }

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("PHP error: " + errorOutput);
        }

        return objectMapper.readValue(output.toString(), PhpFileStructure.class);
    }
}
