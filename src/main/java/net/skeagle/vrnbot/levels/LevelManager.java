package net.skeagle.vrnbot.levels;

import net.skeagle.vrnbot.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    private Map<Long, GuildLeveling> levelMap = new HashMap<>();
    private Connection conn = Bot.db.conn;

    protected void updateGuildId(String column, long guildId) {
        try {
            //language=SQLite
            String s = "INSERT INTO " + column + "(guild_id) VALUES(?)";
            final PreparedStatement ps = conn.prepareStatement(s);
            ps.setString(1, String.valueOf(guildId));
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
