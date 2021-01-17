package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Pause extends VoiceCommand {

    public Pause() {
        super("pause");
        setDesc("Pauses the currently playing track in a voice channel.");
    }

    @Override
    public void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.isPaused())
            returnsend("The player is already paused.");

        if (musicManager.player.getPlayingTrack() != null) {
            musicManager.player.setPaused(true);
            returnsend("**Paused** the currently playing track.");
        }
        send("There is no currently playing track to pause/resume.");
    }
}
