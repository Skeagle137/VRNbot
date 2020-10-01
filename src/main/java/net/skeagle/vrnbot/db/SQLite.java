package net.skeagle.vrnbot.db;

import net.skeagle.vrnbot.settings.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite {

    public Connection conn = null;

    public SQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            System.exit(0);
        }
    }


    private void initializeTable(String name, String columns) {
        //language=SQLite
        String sql = "CREATE TABLE IF NOT EXISTS " + name + " (" + columns + ");";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            System.out.println("DATABASE: table " + name + " loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDefaults() {
        initializeTable("guild_prefix", "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guild_id VARCHAR(20) NOT NULL,prefix VARCHAR(255) NOT NULL DEFAULT '" +
                Config.DEFAULT_PREFIX + "');");
        initializeTable("blacklisted", "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id VARCHAR(20) NOT NULL);");
        initializeTable("guild_logs", "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guild_id VARCHAR(20) NOT NULL,channel_id VARCHAR(30));");
    }
}
