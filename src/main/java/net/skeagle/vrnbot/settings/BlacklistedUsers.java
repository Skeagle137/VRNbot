package net.skeagle.vrnbot.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.skeagle.vrnbot.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlacklistedUsers {

    @Getter
    private static final BlacklistedUsers instance = new BlacklistedUsers();

    private Connection conn = Bot.db.conn;

    public String getBlacklisted(String userId) {
        try {
            //language=SQLite
            String s = "SELECT * FROM blacklisted WHERE user_id = ?";
            final PreparedStatement ps = conn.prepareStatement(s);

            ps.setString(1, String.valueOf(userId));

            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isBlacklisted(String userId) {
        return getBlacklisted(userId) != null;
    }
}