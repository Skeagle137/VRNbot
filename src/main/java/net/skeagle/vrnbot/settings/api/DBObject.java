package net.skeagle.vrnbot.settings.api;

import lombok.Getter;
import net.skeagle.vrnbot.db.DBConnect;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public abstract class DBObject<T> {

    @Getter
    private final String name;
    @Getter
    private final Class<T> objectClass;
    @Getter
    private final Connection conn = DBConnect.getConn();

    public DBObject(String name, Class<T> object) {
        this.name = name;
        this.objectClass = object;
    }

    @Getter
    private final Map<String, T> cacheMap = new HashMap<>();

    protected void save(T object) {
    }

    protected T load() {
        return null;
    }

    protected void delete(T object) {
    }

    protected void update(T object) {
    }

    public void onFinishLoad() {
    }


}
