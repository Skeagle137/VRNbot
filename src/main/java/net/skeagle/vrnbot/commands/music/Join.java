package net.skeagle.vrnbot.commands.music;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import net.skeagle.vrnbot.handlers.Command;

public class Join extends Command {

    public Join() {
        super("join");
        setDesc("Joins a voice channel.");
        setUsage("[channel]");
        setCategory(Category.MUSIC);
    }

    @Override
    public void runCMD() {
        VoiceChannel vc;
        AudioManager am = g.getAudioManager();
        if (am.isConnected()) {
            channel.sendMessage("I am already in a voice channel.").queue();
            return;
        }
        if (args.length > 0) {
            vc = g.getVoiceChannelsByName(args[1], true).get(0);
            if (vc == null) {
                channel.sendMessage("That voice channel does not exist.").queue();
            }
        }
        else {
            vc = isInChannel(g, author);
            if (vc == null) {
                channel.sendMessage("You must be in a voice channel to do this, or you can use v!join [channel].").queue();
            }
        }
        am.openAudioConnection(vc);
    }
}
