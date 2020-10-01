package net.skeagle.vrnbot.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.skeagle.vrnbot.Bot;
import net.skeagle.vrnbot.handlers.AdminCommand;
import net.skeagle.vrnbot.settings.BlacklistedUsers;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlackList extends AdminCommand {

    private Connection conn;

    public BlackList() {
        super("blacklist",
                "Blacklists a user.",
                "<add|remove|status> <userID> [reason]");
        conn = Bot.db.conn;
    }

    @Override
    public void runCMDAdmin() {
        if (args.length < 1) {
            send("You must provide an option. Use either add, remove, or status.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        EmbedBuilder eb = new EmbedBuilder();

        User user = getUser(1);
        switch (args[0].toLowerCase()) {
            case "add":
                if (isBotorOwner(user)) return;
                if (BlacklistedUsers.getInstance().isBlacklisted(user.getId())) {
                    send("Cannot blacklist user, user is already blacklisted.");
                    return;
                }
                eb.setTitle("**You have been blacklisted**", null);
                eb.setDescription("You have been blacklisted from using VRN-bot.");
                eb.setColor(new Color(214, 24, 11));
                if (args.length > 2) {
                    eb.addField("**Reason:**", sb.toString(), false);
                }
                blacklist(user.getId());
                break;
            case "remove":
                if (isBotorOwner(user)) return;
                if (!BlacklistedUsers.getInstance().isBlacklisted(user.getId())) {
                    send("Cannot unblacklist user, user is already not blacklisted.");
                    return;
                }
                eb.setTitle("**You have been unblacklisted**", null);
                eb.setDescription("You can now use VRN-bot again.");
                eb.setColor(new Color(9, 184, 12));
                unblacklist(user.getId());
                break;
            case "status":
                send("**Blacklisted status of " + user.getName() + ":** `" + BlacklistedUsers.getInstance().isBlacklisted(user.getId()) + "`");
                return;
            default:
                send("Invalid option, use either add, remove, or status.");
                return;
        }

        if (g.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE)) {
            msg.delete().queue();
        }
        sendDMEmbed(user, eb.build());
    }

    private void blacklist(String id) {
        try {
            //language=SQLite
            String s = "INSERT INTO blacklisted(user_id) VALUES(?)";
            final PreparedStatement ps = conn.prepareStatement(s);
            ps.setString(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void unblacklist(String id) {
        try {
            //language=SQLite
            String s = "DELETE FROM blacklisted WHERE user_id = ?";
            final PreparedStatement ps = conn.prepareStatement(s);
            ps.setString(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isBotorOwner(User user) {
        if (user.getId().equals(author.getId()) || user.isBot()) {
            send("You can't blacklist yourself or other bots, dumb phony...");
            return true;
        }
        return false;
    }
}
