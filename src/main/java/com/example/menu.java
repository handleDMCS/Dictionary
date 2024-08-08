package com.example;

import java.util.HashMap;
import java.util.Map;

public abstract class menu {
    protected HashMap<String, String> links;

    public abstract String launch();

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displayLinks(String seperator) {
        for (Map.Entry<String, String> entry : links.entrySet()) {
            System.out.println("[" + entry.getKey() + "] " + entry.getValue());
        }
        System.out.println(seperator);
    }

    protected HashMap<String, String> getDefaultLinks() {
        return new HashMap<String, String>() {{
            put(allPhrasesMenu.getAction(), allPhrasesMenu.getLink());
            put(searchMenu.getAction(), searchMenu.getLink());
            put(operationsMenu.getAction(), operationsMenu.getLink());
            put(selectMenu.getAction(), selectMenu.getLink());
            put(mainMenu.getAction(), mainMenu.getLink());
            put(transMenu.getAction(), transMenu.getLink());
            put("e", "Exit");
        }};
    }

    protected HashMap<String, String> getDefaultLinks(String current) {
        HashMap<String, String> result = getDefaultLinks();
        result.remove(current);
        return result;
    }
}