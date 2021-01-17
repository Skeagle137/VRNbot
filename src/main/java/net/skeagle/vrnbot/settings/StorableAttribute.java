package net.skeagle.vrnbot.settings;

import net.skeagle.vrnbot.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class StorableAttribute<T> {

    private final Map<Long, T> map = new HashMap<>();
    private final Connection conn = Bot.db.conn;

    protected abstract String get(long guildid);

    protected Map<Long, T> getMap() {
        return map;
    }

    protected Connection getConn() {
        return conn;
    }

    protected void updateGuildId(String column, long guildId) {
        try {
            String s = "INSERT INTO " + column + "(guild_id) VALUES(?)";
            final PreparedStatement ps = getConn().prepareStatement(s);
            ps.setString(1, String.valueOf(guildId));
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
