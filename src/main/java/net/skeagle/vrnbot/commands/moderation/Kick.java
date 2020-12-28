package net.skeagle.vrnbot.commands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.skeagle.vrnbot.handlers.Command;

import java.awt.*;

public class Kick extends Command {

    public Kick() {
        super("kick");
        setDesc("Kicks a user from the server.");
        setUsage("<user> [reason]");
        setCategory(Category.MODERATION);
    }

    @Override
    public void runCMD() {
        if (args.length < 1) {
            send("You must mention a user or provide a user ID.");
            return;
        }

        User user = getUser(0);
        if (!e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            send("You do not have permission to do this.");
            return;
        }
        if (!g.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            send("Cannot kick the user since I do not have the `Kick Members` permission.");
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**" + user.getName() + "** has been kicked from the server.", null);
        eb.setColor(new Color(214, 24, 11));
        if (args.length > 1)
            eb.addField("**Reason:**", joinArgs(1), false);
        try {
            g.kick(user.getId(), joinArgs(1)).reason(joinArgs(1)).queue(
                    success -> send(eb.build()),
                    failure -> send("Cannot kick the user, do they have higher permissions?"));
        }
        catch (IllegalArgumentException e) {
            send("You cannot kick this user.");
        }
    }
}
