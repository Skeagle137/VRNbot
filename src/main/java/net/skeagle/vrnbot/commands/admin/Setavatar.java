package net.skeagle.vrnbot.commands.admin;

import net.dv8tion.jda.api.entities.*;
import net.skeagle.vrnbot.Bot;
import net.skeagle.vrnbot.handlers.AdminCommand;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class Setavatar extends AdminCommand {
    public Setavatar() {
        super("setavatar",
                "Sets the bot's avatar to the specified URL or user ID.",
                "<default|URL|id:[User ID]>");
    }

    @Override
    public void runCMDAdmin() {
            if (args.length > 0) {
                boolean isID = args[0].startsWith("id:");
                if (isID) {
                    try {
                        args[0] = args[0].replaceFirst("id:", "");
                        Long.parseLong(args[0]);
                    } catch (NumberFormatException e) {
                        send("Could not parse user ID. Are you sure it is a valid user ID?");
                        return;
                    }
                }
                else if (!args[0].equalsIgnoreCase("default")) {
                    try {
                        new URL(args[0]);
                    } catch (MalformedURLException e) {
                        send("Could not parse link. Please check to make sure the URL is valid.");
                        return;
                    }
                }
                Message progress = null;
                try {
                    URLConnection connection;
                    if (isID) {
                        progress = channel.sendMessage("Resolving user avatar...").complete();
                        try {
                            progress.editMessage(progress.getContentRaw() + " " + Objects.requireNonNull(jda.getUserById(args[0])).getAvatarUrl()).complete();
                            connection = new URL(Objects.requireNonNull(Objects.requireNonNull(jda.getUserById(args[0])).getAvatarUrl())).openConnection();
                        } catch (NullPointerException e) {
                            progress.editMessage("That is not an id.").complete();
                            return;
                        }
                    }
                    else {
                        if (args[0].equals("default")) {
                            String pic = (Bot.lite ? "VRNlogolite.png" : "VRNlogo.png");
                            progress = channel.sendMessage("Resetting avatar to default...").complete();
                            progress.editMessage(progress.getContentRaw() + " " + args[0]).complete();
                            connection = getClass().getClassLoader().getResource(pic).openConnection();
                        }
                        else {
                            progress = channel.sendMessage("Resolving URL...").complete();
                            progress.editMessage(progress.getContentRaw() + " " + args[0]).complete();
                            connection = new URL(args[0]).openConnection();
                        }
                    }
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    connection.connect();
                    if (!isID) {
                        if (!connection.getContentType().startsWith("image/")) {
                            progress.editMessage("That is not a valid image.").queue();
                            return;
                        }
                    }
                    progress.editMessage("Applying...").complete();
                    jda.getSelfUser().getManager().setAvatar(Icon.from(connection.getInputStream())).complete();
                    progress.editMessage("Success!").complete();
                } catch (IOException e) {
                    progress.editMessage(":x: " + progress.getContentRaw() + "```" + e.getMessage() + "```").complete();
                }
            }
            else {
                sendDM(author,"You must provide a User ID or URL after the command.");
            }
    }
}
