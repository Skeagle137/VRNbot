package net.skeagle.vrnbot.commands.music;

import net.dv8tion.jda.api.managers.AudioManager;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Leave extends VoiceCommand {

    public Leave() {
        super("leave");
        setDesc("Leaves a voice channel.");
    }

    @Override
    public void runCMDVoice() {
        AudioManager am = g.getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() != null) {
            send("Can't leave the voice channel when there is a currently playing track.");
            return;
        }
        musicManager.scheduler.getQueue().clear();
        musicManager.scheduler.setRepeat(false);
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        am.closeAudioConnection();
    }
}
