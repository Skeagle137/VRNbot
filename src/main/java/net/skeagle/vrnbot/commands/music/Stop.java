package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Stop extends VoiceCommand {

    public Stop() {
        super("stop");
        setDesc("Stops the currently playing music in a channel.");
    }

    @Override
    protected void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() == null) {
            send("There is nothing currently playing.");
            return;
        }
        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        send("Stopped playing and cleared all pending queues.");
    }
}
