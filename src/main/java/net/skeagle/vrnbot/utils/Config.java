package net.skeagle.vrnbot.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv config = Dotenv.load();

    public static final String TOKEN = config.get("token");
    public static final String OWNERID = config.get("ownerid");
    public static final String YTKEY = config.get("ytkey");
    public static final String DEFAULT_PREFIX = config.get("default_prefix");
    public static final String DEFAULT_AVATAR = config.get("default_avatar");
}
