package net.skeagle.vrnbot.commands.music;

import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

public class Volume extends Command {

    public Volume() {
        super("volume|setvolume|vol");
        setDesc("Changes the volume of the player.");
        setUsage("<volume>");
        setCategory(Category.MUSIC);
    }

    @Override
    protected void runCMD() {
        if (args.length > 0) {
            PlayerManager playerManager = PlayerManager.getInstance();
            GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
            int i = 0;
            try {
                i = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                send("That is not a valid number. Volume must be a number 0-200.");
            }
            if (i > 200 || i < 0) {
                send("Volume must be a number 0-200.");
            }
            musicManager.player.setVolume(i);
            send("Set volume to `" + musicManager.player.getVolume() + "`.");
            return;
        }
        send("You need to provide a number 0-200 for the new volume.");
    }
}
