package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Stop extends Command {

    public Stop() {
        super("stop");
        setDesc("Stops the currently playing music in a channel.");
        setCategory(Category.MUSIC);
    }

    @Override
    protected void runCMD() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        send("Stopped playing and cleared all pending queues.");
    }
}
