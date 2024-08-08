package com.example;

import java.util.Scanner; 

class selectMenu extends menu {
    private DB data_source;

    public selectMenu() {
        this.data_source = DB.getInstance();
        this.links = getDefaultLinks(getAction());
    }

    public static String getAction() {
        return "p";
    }

    public static String getLink() {
        return "Pick a phrase";
    }

    private void updateDefinition(String phrase) { 
        pyScriptRunner script = new pyScriptRunner("updateDef.py", "updateDef.json");
        try {
            script.launch(phrase);
            data_source.insertJSON(script.getOutputPath(), true);
            script.remove();
            System.out.println("Definition updated > '" + phrase + " : " + data_source.getDef(phrase) + "'");
            System.out.println();
            System.out.println("Press enter to continue ...");
        } catch(Exception e) {
            System.out.println();
            System.out.println("Update failed, press enter to continue ...");
        }
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private void giveExample(String phrase) {
        pyScriptRunner script = new pyScriptRunner("example.py", "example.json");
        try {
            System.out.println("Example : ");
            script.launch(phrase); 
            System.out.println();
            System.out.println("Press enter to continue ...");
        } catch(Exception e) {
            System.out.println();
            System.out.println("Something went wrong :( Press enter to continue ...");
        }
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); 
    }

    private void navigate(String phrase) {
        System.out.println();
        System.out.println("Selected > '" + phrase + " : " + data_source.getDef(phrase) + "'");
        System.out.println("[d] Delete this phrase");
        System.out.println("[u] Update the definition");
        System.out.println("[g] Give an example");
        System.out.println();
        System.out.println("What to do next ?");
        Scanner scanner = new Scanner(System.in);
        String op = scanner.nextLine();
        switch (op) {
            case "d":
                data_source.remove(phrase);
                System.out.println("Entry deleted ! Press enter to continue ...");
                scanner.nextLine();
                return ;
            
            case "u":
                updateDefinition(phrase);
                return ;

            case "g":
                giveExample(phrase);
                return ;

            default:
                return ;
        }
    }

    @Override
    public String launch() {
        clear();
        System.out.println("> " + getLink() + " \n");
        displayLinks("");
        System.out.println("Go to (or press enter to pick a phrase) :");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        if(links.containsKey(action)) {
            return links.get(action);
        }

        System.out.println("Please enter the phrase : ");
        String phrase = scanner.nextLine();
        if(data_source.existPhrase(phrase) == false) {
            System.out.println("No results found, press enter to continue ...");
            scanner.nextLine();
            return "Reload";
        } 
        navigate(phrase);
        
        return "Reload";
    }
}