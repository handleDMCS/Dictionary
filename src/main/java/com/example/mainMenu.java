package com.example;

import java.util.Scanner; 

class mainMenu extends menu {
    public mainMenu() {
        this.links = getDefaultLinks(getAction());
    }

    public static String getAction() {
        return "m";
    }

    public static String getLink() {
        return "Main menu";
    }

    @Override
    public String launch() {
        clear();
        System.out.println("> " + getLink() + " \n");
        displayLinks("");

        System.out.println("Go to : ");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();
        if(links.containsKey(action)) {
            return links.get(action);
        }
        return "Reload";
    }
}