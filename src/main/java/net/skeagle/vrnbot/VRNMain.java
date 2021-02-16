package net.skeagle.vrnbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.skeagle.vrnbot.db.DBConnect;
import net.skeagle.vrnbot.handlers.Listeners;
import net.skeagle.vrnbot.utils.Config;

import javax.security.auth.login.LoginException;

public final class VRNMain extends ListenerAdapter {

    public static void main(String[] args) {
        try {
            DBConnect.getInstance().load();
            JDABuilder.createDefault(Config.TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MEMBERS)
                    .setActivity(Activity.watching("your every move."))
                    .setCompression(Compression.NONE)
                    .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING,
                            GatewayIntent.GUILD_INVITES)
                    .addEventListeners(new Listeners()).build();
        } catch (LoginException e) {
            System.out.println("\nERROR: Login failed.");
            e.printStackTrace();
        }
    }
}
