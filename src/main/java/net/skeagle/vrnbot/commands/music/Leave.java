package net.skeagle.vrnbot.commands.music;

import net.dv8tion.jda.api.managers.AudioManager;

import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Leave extends Command {

    public Leave() {
        super("leave");
        setDesc("Leaves a voice channel.");
        setCategory(Category.MUSIC);
    }

    @Override
    public void runCMD() {
        AudioManager am = g.getAudioManager();
        if (!am.isConnected()) {
            send("I am not currently in a voice channel.");
            return;
        }
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        am.closeAudioConnection();
    }
}
