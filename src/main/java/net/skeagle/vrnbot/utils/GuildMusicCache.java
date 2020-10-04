package net.skeagle.vrnbot.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuildMusicCache {
    @Getter
    private static final GuildMusicCache instance = new GuildMusicCache();

    @Getter
    HashMap<String, Integer> volumeCache = new HashMap<>();
}
