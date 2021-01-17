package net.skeagle.vrnbot.settings;

import lombok.Getter;
import net.skeagle.vrnbot.levels.LevelManager;
import net.skeagle.vrnbot.utils.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Leveling extends StorableAttribute<LevelManager> {

    @Getter
    private static final Leveling instance = new Leveling();

    @Getter
    private final Map<Long, LevelManager> leveling_map = getMap();

    public String get(long guildId) {
        try {
            String s = "SELECT prefix FROM guild_prefix WHERE guild_id = ?";
            final PreparedStatement ps = getConn().prepareStatement(s);
            ps.setString(1, String.valueOf(guildId));
            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("prefix");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateGuildId("guild_prefix", guildId);
        return Config.DEFAULT_PREFIX;
    }

}