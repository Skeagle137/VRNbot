package net.skeagle.vrnbot.settings;

import lombok.Getter;

@Getter
public enum CachedRole {
    DJ("DJ"),
    MUTED("muted", 65536);

    private final String defaultname;
    private long perms;

    CachedRole(String defaultname) {
        this.defaultname = defaultname;
    }

    CachedRole(String defaultname, long perms) {
        this.defaultname = defaultname;
        this.perms = perms;
    }
}
