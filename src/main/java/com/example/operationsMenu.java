package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class operationsMenu extends menu {
    private DB data_source;

    public operationsMenu() {
        this.data_source = DB.getInstance();
        this.links = getDefaultLinks(getAction());
    }

    public static String getAction() {
        return "o";
    }

    public static String getLink() {
        return "Other operations";
    }

    private void exportData() {
        System.out.println("Please specify the directory : (e.g C:\\Users\\UserName\\Documents\\...)");
        Scanner scanner = new Scanner(System.in);
        String dirPath = scanner.nextLine();
        if (!Files.exists(Paths.get(dirPath)) || !Files.isDirectory(Paths.get(dirPath))) {
            System.out.println("Directory doesn't exist ! Press enter to continue ...");
            scanner.nextLine();
            return ;
        }
        System.out.println("Output file name : ('output.json' by default)");
        String op = scanner.nextLine();
        if (op.trim().isEmpty()) {
            op = "output";
        }
        if (!op.toLowerCase().endsWith(".json")) {
            op += ".json";
        }

        String outputPath = op;
        if(!dirPath.trim().isEmpty()) {
            outputPath = dirPath + File.separator + op;
        }

        Map<String, String> wordMap = new HashMap<>();
        try {
            List<Word> data = data_source.fetch();
            for (Word word : data) {
                wordMap.put(word.getWordTarget(), word.getWordExplain());
            }
        } catch(Exception e) {
            System.out.println();
            System.out.println("Nothing to export !!! Press enter to continue ...");
            scanner.nextLine();
            return ;
        }
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(wordMap);
        
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(jsonString);
            System.out.println();
            System.out.println("Data exported to: " + outputPath);
            System.out.println("Press enter to continue ...");
            scanner.nextLine();
        } catch (IOException e) {
            System.out.println("Error occurred while exporting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importData() {
        System.out.println();
        System.out.println("Enter the path to your input file (json / txt): ");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.nextLine();
        if(inputPath.endsWith(".txt")) {
            pyScriptRunner script = new pyScriptRunner("importTxt.py", "importTxt.json");
            try {
                script.launch(inputPath);
                int inserted = data_source.insertJSON(script.getOutputPath(), false);
                System.out.println("> " + inserted + " new keywords inserted into the database");
                script.remove();
                System.out.println();
                System.out.println("Press enter to continue ...");
            } catch(Exception e) {
                System.out.println();
                System.out.println("Something went wrong :( Press enter to continue ...");
            }
            scanner.nextLine();
        } else if(inputPath.endsWith(".json")) {
            int inserted = data_source.insertJSON(inputPath, false);
            System.out.println("> " + inserted + " new keywords inserted into the database");
            System.out.println();
            System.out.println("Press enter to continue ...");
            scanner.nextLine();
        } else {
            System.out.println();
            System.out.println("Invalid file extension :( Press enter to continue ...");
            scanner.nextLine();
        }
    }

    private void addEntries() {
        pyScriptRunner script = new pyScriptRunner("newEntries.py", "newEntries.json");
        try {
            script.launch();
            int inserted = data_source.insertJSON(script.getOutputPath(), false);
            System.out.println("> " + inserted + " new keywords inserted into the database");
            script.remove();
            System.out.println();
            System.out.println("Press enter to continue ...");
        } catch(Exception e) {
            System.out.println();
            System.out.println("Something went wrong :( Press enter to continue ...");
        }   
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    @Override
    public String launch() {
        clear();
        System.out.println("> " + getLink() + " \n");
        displayLinks("----------");
        System.out.println("[add] Add new dictionary entries");
        System.out.println("[imp] Import data (json / txt)");
        System.out.println("[exp] Export the database (json)");
        System.out.println();
        System.out.println("Go to : ");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        if(links.containsKey(action)) {
            return links.get(action);
        } 

        switch (action) {
            case "add":
                addEntries();
                break;
            case "imp":
                importData();
                break;
            case "exp":
                exportData();
                break;
        
            default:
                break;
        }

        return "Reload";
    }
}