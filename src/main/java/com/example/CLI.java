package com.example;

import java.util.HashMap;

class CLI {
    private HashMap<String, menu> all_menu;
    private menu current_menu;

    public CLI() {
        init();
        launch();
    }

    private void init() {
        this.all_menu = new HashMap<String, menu>() {{
            put(mainMenu.getLink(), new mainMenu());
            put(searchMenu.getLink(), new searchMenu());
            put(allPhrasesMenu.getLink(), new allPhrasesMenu());
            put(operationsMenu.getLink(), new operationsMenu());
            put(selectMenu.getLink(), new selectMenu());
            put(transMenu.getLink(), new transMenu());
        }};

        current_menu = all_menu.get(mainMenu.getLink());
    }

    private void launch() {
        while(true) {
            String redirect = current_menu.launch();
            if(redirect == "Reload") {
                continue ;
            }
            if(redirect == "Exit") {
                return ;
            }
            current_menu = all_menu.get(redirect);
        }
    }
}