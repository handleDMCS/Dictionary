package com.example;

import java.util.Scanner;
import java.util.HashMap;
import java.util.List;

class allPhrasesMenu extends menu {
    private int first_index;
    private int words_per_page;
    private List<Word> page;
    private DB data_source;
    private HashMap<String, Object> props;

    public allPhrasesMenu() {
        this.first_index = 0;
        this.words_per_page = 10;
        this.data_source = DB.getInstance();
        this.links = getDefaultLinks(getAction());
    }

    public static String getAction() {
        return "a";
    }

    public static String getLink() {
        return "All phrases";
    }

    private Integer convertStringToInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void navigate(String action) {
        int new_first_index = first_index;
        switch (action) {
            case "z":
                new_first_index -= words_per_page;
                break;
            case "c":
                new_first_index += words_per_page;
                break;
            default:
                if(convertStringToInteger(action) == null) {
                    return ;
                }
                int page_id = convertStringToInteger(action).intValue();
                new_first_index = (page_id-1) * words_per_page;
                break;
        }
        List<Word> new_page = data_source.fetch(new_first_index, new_first_index+words_per_page-1);
        if(new_page != null) {
            first_index = new_first_index;
            page = new_page;
        }
    }

    @Override
    public String launch() {
        int page_id = (first_index / words_per_page) + 1;
        if(this.page == null) {
            this.page = data_source.fetch(first_index, first_index+words_per_page-1);
        }

        clear();
        System.out.println("> " + getLink() + " \n");
        
        Scanner scanner = new Scanner(System.in);
        String action;
        displayLinks("----------");
        System.out.println("[id] Page with the corresponding 'id' (e.g., [123])");
        System.out.println("[z] Previous page");
        System.out.println("[c] Next page");
        System.out.println();
        System.out.println("> Page " + page_id);
        System.out.println();
        try {
            for(Word entry: page) {
                System.out.println(entry.getWordTarget() + " : " + entry.getWordExplain());
            }
        } catch(Exception e) {
            
        }
        System.out.println();
        System.out.println("Go to :");
        action = scanner.nextLine();

        if(links.containsKey(action)) {
            return links.get(action);
        }
        navigate(action);
        return "Reload";
    }
}