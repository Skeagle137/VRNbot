package net.skeagle.vrnbot.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.skeagle.vrnbot.utils.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Prefix extends StorableAttribute {

    @Getter
    private static final Prefix instance = new Prefix();

    @Getter
    private final Map<Long, String> prefixes = getMap();

    public String get(long guildId) {
        try {
            //language=SQLite
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
