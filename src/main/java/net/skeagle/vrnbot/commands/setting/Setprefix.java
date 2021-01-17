package net.skeagle.vrnbot.commands.setting;

import net.skeagle.vrnbot.Bot;
import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.settings.Prefix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Setprefix extends Command {

    private Connection conn;

    public Setprefix() {
        super("setprefix|prefix");
        setDesc("Set the guild prefix of the bot.");
        setUsage("<prefix>");
        setCategory(Category.SETTINGS);
        conn = Bot.db.conn;
    }

    @Override
    public void runCMD() {
        if (args.length < 1)
            returnsend("You must provide a new prefix.");

        setPrefix(g.getIdLong(), args[0]);
        send("Set the prefix to `" + args[0] + "`.");
    }

    private void setPrefix(long id, String prefix) {
        Prefix.getInstance().getPrefixes().put(id, prefix);

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE guild_prefix SET prefix = ? WHERE guild_id = ?");
            ps.setString(1, prefix);
            ps.setString(2, String.valueOf(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
