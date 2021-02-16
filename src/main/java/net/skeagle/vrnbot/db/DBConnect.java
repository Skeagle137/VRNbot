package net.skeagle.vrnbot.db;

import lombok.Getter;
import net.skeagle.vrnbot.settings.BlacklistedUsers;
import net.skeagle.vrnbot.settings.api.DBObject;
import net.skeagle.vrnbot.settings.api.SkipPrimaryID;
import net.skeagle.vrnbot.settings.guildsettings.GuildSettings;
import net.skeagle.vrnbot.utils.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DBConnect {

    private final List<DBObject<?>> tablesList = new ArrayList<>();
    @Getter
    private static Connection conn;
    @Getter
    private final static DBConnect instance = new DBConnect();

    public void load() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            System.exit(0);
        }
        registerTable(new GuildSettings());
        registerTable(new BlacklistedUsers());
    }


    private <T extends DBObject<?>> void registerTable(T table) {
        if (tablesList.contains(table))
            throw new RuntimeException("Database already registered:" + table.getName());
        tablesList.add(table);
        try {
            initializeTable(table.getName(), table.getObjectClass());
            table.onFinishLoad();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTable(String name, Class<?> clazz) {
        StringBuilder columns = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (Modifier.isPrivate(fields[i].getModifiers())) {
                if (!fields[i].getType().isPrimitive()) {
                    if (!clazz.isAnnotationPresent(SkipPrimaryID.class)) {
                        columns.append(i != 0 ? "" : "id INTEGER PRIMARY KEY AUTOINCREMENT,")
                                .append(fields[i].getName().toLowerCase())
                                .append(" ")
                                .append(Conversions.getTypeStringFromClass(fields[i].getType()))
                                .append(i != fields.length - 1 ? "," : "");
                    }
                    else {
                        columns.append(fields[i].getName().toLowerCase())
                                .append(" ")
                                .append(Conversions.getTypeStringFromClass(fields[i].getType()))
                                .append(i != 0 ? "" : " PRIMARY KEY")
                                .append(i != fields.length - 1 ? "," : "");
                    }
                }
                else
                    throw new RuntimeException("The field " + fields[i].getName() + " in " + clazz.getName() + " cannot be primitive.");
            }
        }
        String sql = "CREATE TABLE IF NOT EXISTS " + name + " (" + columns.toString() + ");";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM " + name);
            ResultSet rs = ps2.executeQuery();
            if (fields.length > rs.getMetaData().getColumnCount() - (clazz.isAnnotationPresent(SkipPrimaryID.class) ? 0 : 1)) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs2;
                for (Field f : fields) {
                    rs2 = meta.getColumns(null, null, name, f.getName().toLowerCase());
                    if (rs2.next())
                        continue;
                    PreparedStatement ps3 = conn.prepareStatement("ALTER TABLE " + name + " ADD COLUMN " + f.getName().toLowerCase());
                    ps3.executeUpdate();
                    System.out.println(name + " database updated new column " + f.getName().toLowerCase());
                }
            }
            System.out.println(name + " database loaded.");

        } catch (SQLException e) {
            System.out.println("Could not load " + name + " database.");
            e.printStackTrace();
        }
    }

    private enum Conversions {
        STRING("VARCHAR(255)", String.class),
        INTEGER("INTEGER", Integer.class),
        DOUBLE("INTEGER", Double.class),
        LONG("INTEGER", Long.class),
        FLOAT("INTEGER", Float.class),
        BOOLEAN("BOOL", Boolean.class);

        private String typeString;
        private Class<?> serializedDataType;

        Conversions(final String typeString, final Class<?> serializedDataType) {
            this.typeString = typeString;
            this.serializedDataType = serializedDataType;
        }

        static String getTypeStringFromClass(Class<?> clazz) {
            for (Conversions c : Conversions.values())
                if (c.serializedDataType == clazz)
                    return c.typeString;
            return STRING.typeString;
        }
    }
}
