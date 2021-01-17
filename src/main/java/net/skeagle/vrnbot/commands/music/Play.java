package net.skeagle.vrnbot.commands.music;

import net.dv8tion.jda.api.managers.AudioManager;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;
import net.skeagle.vrnbot.utils.GuildMusicCache;

import java.net.MalformedURLException;
import java.net.URL;

public class Play extends VoiceCommand {

    public Play() {
        super("play|p", true, false, true);
        setDesc("Plays music through youtube, soundcloud, spotify, etc from a URL or search using keywords");
        setUsage("<url|keywords>");
        setCategory(Category.MUSIC);
    }

    @Override
    public void runCMDVoice() {
        if (args.length < 1)
            returnsend("You must include a URL or keywords in the command.");

        AudioManager am = g.getAudioManager();
        if (!am.isConnected())
            am.openAudioConnection(e.getMember().getVoiceState().getChannel());

        PlayerManager manager = PlayerManager.getInstance();

        String s = parseSearch(String.join(" ", args));
        manager.loadAndPlay(channel, s);

        if (!GuildMusicCache.getInstance().getVolumeCache().containsKey(g.getId())) {
            GuildMusicCache.getInstance().getVolumeCache().put(g.getId(), 50);
            manager.getGuildMusicManager(g).player.setVolume(50);
        }
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        musicManager.player.setPaused(false);
    }

    private String parseSearch(String s) {
        if (!isUrl(s)) {
            s = "ytsearch:" + s;
        }
        return s;
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }
}