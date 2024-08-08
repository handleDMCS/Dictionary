package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class pyScriptRunner {
    private String scriptPath; 
    private String outputPath; 
    
    public pyScriptRunner(String scriptPath, String outputPath) {
        this.scriptPath = scriptPath;
        this.outputPath = outputPath;
    }
    
    public String getOutputPath() {
        return outputPath;
    }

    public void launch(String ...args) throws Exception {
        List<String> command = new ArrayList<>();
        command.add("python");  // or "python3"
        command.add(scriptPath);
        command.add(outputPath);
        command.addAll(List.of(args));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();
    }

    public void remove() throws Exception {
        Path path = Paths.get(outputPath);
        Files.deleteIfExists(path);
    }    
}