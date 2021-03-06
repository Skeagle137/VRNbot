package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.VRNException;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;
import net.skeagle.vrnbot.utils.GuildMusicCache;

public class Volume extends VoiceCommand {

    public Volume() {
        super("volume|setvolume|vol");
        setDesc("Changes the volume of the player.");
        setUsage("<volume>");
    }

    @Override
    protected void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() == null)
            returnsend("Cannot change the volume when there is no currently playing track.");

        if (args.length < 1)
            returnsend("You need to provide a number 0-100 for the new volume.");

        int i;
        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new VRNException("That is not a valid number. Volume must be a number 0-100.");
        }
        if (i > 100 || i < 0)
            returnsend("Volume must be a number 0-100.");

        GuildMusicCache.getInstance().getVolumeCache().put(g.getId(), i);
        musicManager.player.setVolume(i);
        send("Set volume to `" + musicManager.player.getVolume() + "`.");
    }
}
