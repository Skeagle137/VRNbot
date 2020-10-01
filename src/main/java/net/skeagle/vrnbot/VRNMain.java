package net.skeagle.vrnbot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.skeagle.vrnbot.db.SQLite;

public class VRNMain extends ListenerAdapter {

    public static void main(String[] args) {
        SQLite db = new SQLite();
        db.createDefaults();
        new Bot(db).connect();
    }
}
