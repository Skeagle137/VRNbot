package net.skeagle.vrnbot.settings.guildsettings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.skeagle.vrnbot.settings.CachedRole;
import net.skeagle.vrnbot.settings.api.DBObject;
import net.skeagle.vrnbot.utils.Config;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GuildSettings extends DBObject<Settings> {

    public GuildSettings() {
        super("settings", Settings.class);
    }

    @Getter
    private static final GuildSettings instance = new GuildSettings();

    public Settings getSettings(Guild g) {

        Settings settings = instance.getCacheMap().get(g.getId());

        if (settings == null) {
            settings = instance.loadData(g);

            instance.getCacheMap().put(g.getId(), settings);
        }

        return settings;
    }

    private Settings loadData(Guild g) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE id = " + g.getId());
            try (final ResultSet rs = ps.executeQuery()) {
                Gson gson = new Gson();
                Type listtype = new TypeToken<List<String>>(){}.getType();
                return new Settings(g.getId(), rs.getString("prefix"),
                        rs.getBoolean("antispam"),
                        gson.fromJson(rs.getString("spamchannels"), listtype),
                        gson.fromJson(rs.getString("rolecache"), new TypeToken<Map<CachedRole, String>>(){}.getType()),
                        gson.fromJson(rs.getString("defaultroles"), listtype));
            }
        }
        catch (SQLException e) {
            try {
                PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() +
                        " (id, prefix, antispam, spamchannels, rolecache, defaultroles) " +
                        "VALUES(?, ?, ?, ?, ?, ?)");
                ps.setString(1, g.getId());
                ps.setString(2, Config.DEFAULT_PREFIX);
                ps.setBoolean(3, false);
                ps.setString(4, "[]");
                ps.setString(5, "{}");
                ps.setString(6, "[]");
                ps.execute();
            }
            catch (Exception e2) {
                System.out.println("Could not create player data");
                e2.printStackTrace();
            }
        }
        return loadData(g);
    }
}
