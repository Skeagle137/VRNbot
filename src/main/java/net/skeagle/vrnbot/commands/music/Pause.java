package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Pause extends Command {

    public Pause() {
        super("pause");
        setDesc("Pauses the currently playing track in a voice channel.");
        setCategory(Category.MUSIC);
    }

    @Override
    public void runCMD() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() != null) {
            musicManager.player.setPaused(!musicManager.player.isPaused());
            send("**" + (musicManager.player.isPaused() ? "Paused" : "Resumed") + "** the currently playing track.");
            return;
        }
        send("There is no currently playing track to pause/resume.");
    }
}
