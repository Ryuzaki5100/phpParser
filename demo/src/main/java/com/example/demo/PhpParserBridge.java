package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PhpParserBridge {

    public static PhpFileStructure runPhpParser(String phpCode) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("php", "php/parse.php");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(phpCode);
        }

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("PHP Parser process exited with code " + exitCode);
        }

        // Deserialize JSON to PhpFileStructure
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(output.toString(), PhpFileStructure.class);
    }

    public static void main(String[] args) {
        String phpCode = "<?php\n"
                + "include \"configuration.php\";\n"
                + "use Helpers\\Adaptors\\CommonAdaptor;\n"
                + "CommonAdaptor::setPortCookie($vars->adaptor_port);\n";

        try {
            PhpFileStructure structure = runPhpParser(phpCode);
            System.out.println("Parsed PHP Structure:\n" + new GsonBuilder().setPrettyPrinting().create().toJson(structure));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
