package net.skeagle.vrnbot.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuildMusicCache {

    @Getter
    private static final GuildMusicCache instance = new GuildMusicCache();

    @Getter
    HashMap<String, Integer> volumeCache = new HashMap<>();
    @Getter
    List<String> repeatCache = new ArrayList<>();
}
