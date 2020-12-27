package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Repeat extends VoiceCommand {

    public Repeat() {
        super("repeat|loop");
        setDesc("Make the currently playing track repeat when it ends.");
    }

    @Override
    public void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() == null) {
            send("There is no currently playing track.");
            return;
        }
        musicManager.scheduler.setRepeat(!musicManager.scheduler.isRepeat());
        send("The currently playing track will " + (musicManager.scheduler.isRepeat() ? "now" : "no longer") + " repeat.");
    }
}
