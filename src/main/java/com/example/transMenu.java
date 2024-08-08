package com.example;

import java.util.Scanner;

public class transMenu extends menu {
    private DB data_source;

    public transMenu() {
        this.data_source = DB.getInstance();
        this.links = getDefaultLinks(getAction());
    }


    public static String getAction() {
        return "t";
    }

    public static String getLink() {
        return "Translate & pronounce";
    }

    private void translate(String action) {
        pyScriptRunner script = new pyScriptRunner("trans.py", "trans.json");
        try {
            script.launch(action);
            System.out.println();
            System.out.println("Press enter to continue ...");
        } catch(Exception e) {
            System.out.println();
            System.out.println("Something went wrong :( Press enter to continue ...");
        } 
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private void pronounce() {
        pyScriptRunner script = new pyScriptRunner("textToSpeech.py", "textToSpeech.json");
        try {
            script.launch();
            System.out.println();
            System.out.println("> Audio playing ...");
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
        System.out.println("[en] From ... to english (translation)");
        System.out.println("[vi] From ... to vietnamese (translation)");
        System.out.println("[pro] Pronunciation (english)");
        System.out.println();
        System.out.println("Go to : ");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        if(links.containsKey(action)) {
            return links.get(action);
        }

        if(action.equals("vi") || action.equals("en")) {
            translate(action);
        }
        if(action.equals("pro")) {
            pronounce();
        }
        
        return "Reload";
    }
}