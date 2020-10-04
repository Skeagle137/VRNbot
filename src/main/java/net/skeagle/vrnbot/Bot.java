package net.skeagle.vrnbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.skeagle.vrnbot.db.SQLite;
import net.skeagle.vrnbot.handlers.Listeners;
import net.skeagle.vrnbot.settings.Config;

import javax.security.auth.login.LoginException;

public final class Bot {

    public static JDABuilder jda;
    public static SQLite db;

    Bot(SQLite db) {
        Bot.db = db;
    }

    void connect() {
        try {
        JDABuilder.createDefault(Config.TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setActivity(Activity.watching("your every move."))
                .setCompression(Compression.NONE)
                .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Listeners()).build();
        } catch (LoginException e) {
            System.out.println("\nERROR: Login failed.");
            e.printStackTrace();
        }
    }
}
