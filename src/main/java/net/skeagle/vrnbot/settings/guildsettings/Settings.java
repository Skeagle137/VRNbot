package net.skeagle.vrnbot.settings.guildsettings;

import com.google.gson.Gson;
import lombok.Getter;
import net.skeagle.vrnbot.db.DBConnect;
import net.skeagle.vrnbot.settings.CachedRole;
import net.skeagle.vrnbot.settings.api.SkipPrimaryID;
import net.skeagle.vrnbot.settings.api.StoreableObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@SkipPrimaryID @Getter
public class Settings extends StoreableObject<Settings> {
    private final String id;
    private String prefix;
    private Boolean antispam;
    private List<String> spamchannels;
    private Map<CachedRole, String> rolecache;
    private List<String> defaultroles;

    public Settings(String id, String prefix, Boolean antispam, List<String> spamchannels, Map<CachedRole, String> rolecache, List<String> defaultroles) {
        this.id = id;
        this.prefix = prefix;
        this.antispam = antispam;
        this.spamchannels = spamchannels;
        this.rolecache = rolecache;
        this.defaultroles = defaultroles;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        update("prefix", prefix);
    }

    public void setDefaultroles(List<String> defaultroles) {
        this.defaultroles = defaultroles;
        update("defaultroles", new Gson().toJson(defaultroles));
    }

    public void setAntispam(Boolean antispam) {
        this.antispam = antispam;
        update("antispam", antispam);
    }

    public void setSpamchannels(List<String> spamchannels) {
        this.spamchannels = spamchannels;
        update("spamchannels", new Gson().toJson(spamchannels));
    }

    public void setRolecache(CachedRole role, String id) {
        if (rolecache.get(role) != null)
            rolecache.put(role, id);
        update("rolecache", new Gson().toJson(rolecache));
    }

    private void update(String s, Object o) {
        try {
            PreparedStatement ps = DBConnect.getConn().prepareStatement("UPDATE settings SET " + s + " = ? WHERE id = ?");
            if (o == null)
                ps.setNull(1, Types.NULL);
            else {
                if (o instanceof Boolean)
                    ps.setBoolean(1, (Boolean) o);
                else
                    ps.setString(1, (String) o);
            }
            ps.setString(2, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Could not save guild settings.");
            e.printStackTrace();
        }
    }
}
