package net.skeagle.vrnbot.commands.admin;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.skeagle.vrnbot.handlers.AdminCommand;
import net.skeagle.vrnbot.utils.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
        if (args.length < 1)
            send("You must provide a User ID or URL after the command.");
        else if (args[0].equalsIgnoreCase("default"))
            updateAvatar(false, true, null);
        else {
            try {
                new URL(args[0]);
                updateAvatar(true, false, null);
            } catch (MalformedURLException e) {
                User user = getUser(0);
                if (user != null) updateAvatar(false, false, user);
            }
        }
    }

    private void updateAvatar(boolean isURL, boolean default_pic, User user) {
        Message progress = null;
        try {
            URLConnection connection;
            if (!isURL) {
                if (default_pic) {
                    String pic = Config.DEFAULT_AVATAR;
                    progress = channel.sendMessage("Resetting avatar to default...").complete();
                    progress.editMessage(progress.getContentRaw() + " " + args[0]).complete();
                    connection = getClass().getClassLoader().getResource(pic).openConnection();
                }
                else {
                    progress = channel.sendMessage("Resolving image...").complete();
                    progress.editMessage(progress.getContentRaw() + " " + user.getAvatarUrl()).complete();
                    connection = new URL(user.getAvatarUrl()).openConnection();
                }
            }
            else {
                progress = channel.sendMessage("Resolving URL...").complete();
                progress.editMessage(progress.getContentRaw() + " " + args[0]).complete();
                connection = new URL(args[0]).openConnection();
            }
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            if (isURL) {
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
        catch (ErrorResponseException e) {
            progress.editMessage(":x: `You are doing that too fast, slow down.`").complete();
        }
    }
}
