package com.example;

import java.util.Scanner; 
import java.util.List;

class searchMenu extends menu {
    private DB data_source;

    public searchMenu() {
        this.data_source = DB.getInstance();
        this.links = getDefaultLinks(getAction());
    }

    public static String getAction() {
        return "s";
    }

    public static String getLink() {
        return "Search ...";
    }

    @Override
    public String launch() {
        clear();
        System.out.println("> " + getLink() + " \n");
        String action, keyword;
        displayLinks("----------");
        System.out.println("[pre] Prefix search");
        System.out.println("[sub] Substring search");
        System.out.println();
        System.out.println("Go to : ");
        
        Scanner scanner = new Scanner(System.in);
        action = scanner.nextLine();
        if(links.containsKey(action)) {
            return links.get(action);
        }

        List<Word> data = null;

        switch (action) {
            case "pre":
                System.out.println("Keyword : ");
                keyword = scanner.nextLine();
                data = data_source.search(keyword, "prefix");
                break;
            case "sub":
                System.out.println("Keyword : ");
                keyword = scanner.nextLine();
                data = data_source.search(keyword, "substring");
                break;
        
            default:
                return "Reload";
        }

        System.out.println("-----------------------------------------------------");
        try {
            for(Word entry: data) {
                System.out.println("\t" + entry.getWordTarget() + " : " + entry.getWordExplain());
            } 
            if (data.size() == 0) {
                System.out.println("\tNo results found !!!");
            }
        } catch(Exception e) {
            System.out.println("\tNo results found !!!");
        }
        System.out.println("-----------------------------------------------------");
        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();

        return "Reload";
    }
}