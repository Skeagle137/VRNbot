package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Unpause extends VoiceCommand {

    public Unpause() {
        super("unpause|resume");
        setDesc("Resumes the currently playing track in a voice channel.");
    }

    @Override
    public void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (!musicManager.player.isPaused())
            returnsend("The player is not currently paused.");

        if (musicManager.player.getPlayingTrack() != null)
            musicManager.player.setPaused(false);
            returnsend("**Resumed** the currently playing track.");

        send("There is no currently playing track to pause/resume.");
    }
}
