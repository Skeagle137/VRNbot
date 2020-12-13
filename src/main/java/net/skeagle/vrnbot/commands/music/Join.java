package net.skeagle.vrnbot.commands.music;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.skeagle.vrnbot.handlers.VoiceCommand;

import java.util.List;

public class Join extends VoiceCommand {

    public Join() {
        super("join", true, false, false);
        setDesc("Joins the voice channel that you are in.");
    }

    @Override
    public void runCMDVoice() {
        AudioManager am = g.getAudioManager();
        if (am.isConnected()) {
            send((!isInSameChannel(g, e.getMember()) ?
                    "I am already in a voice channel. Use the summon command to summon me to your channel." : "I am already in your voice channel."));
            return;
        }
        am.openAudioConnection(e.getMember().getVoiceState().getChannel());
    }
}
