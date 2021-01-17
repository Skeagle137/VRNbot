package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Skip extends VoiceCommand {

    public Skip() {
        super("skip");
        setDesc("Stop the currently playing track and go to the next track in the queue.");
    }

    @Override
    public void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() == null)
            returnsend("There is no currently playing track to skip.");

        musicManager.scheduler.setRepeat(false);
        send("Skipped the track." + (musicManager.scheduler.getQueue().isEmpty() ? " There are no more tracks left in the queue to play." : ""));
        musicManager.scheduler.nextTrack();
    }
}
