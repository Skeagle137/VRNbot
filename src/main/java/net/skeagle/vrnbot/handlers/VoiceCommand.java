package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class VoiceCommand extends Command {

    protected JDA jda;
    protected String[] args;
    protected TextChannel channel;
    protected User author;
    protected Message msg;
    protected Guild g;
    protected GuildMessageReceivedEvent e;
    private final boolean isInChannel, isBotInChannel, isInSameChannel;

    public VoiceCommand(String name) {
        super(name);
        setCategory(Category.MUSIC);
        this.isInChannel = true;
        this.isBotInChannel = true;
        this.isInSameChannel = true;
    }

    public VoiceCommand(String name, boolean isInChannel, boolean isBotInChannel, boolean isInSameChannel) {
        super(name);
        this.isInChannel = isInChannel;
        this.isBotInChannel = isBotInChannel;
        this.isInSameChannel = isInSameChannel;
        setCategory(Category.MUSIC);
    }

    @Override
    protected void runCMD() {
        this.jda = super.jda;
        this.args = super.args;
        this.channel = super.channel;
        this.author = super.author;
        this.msg = super.msg;
        this.g = super.g;
        this.e = super.e;
        if (isInChannel)
            if (e.getMember().getVoiceState().getChannel() == null)
                returnsend("You must be in a voice channel to do this.");
        if (!g.getAudioManager().isConnected() && isBotInChannel)
            returnsend("I am not currently in a voice channel.");
        if (!isInSameChannel(g, e.getMember()) && g.getAudioManager().isConnected() && isInSameChannel)
            returnsend("You must be in the same voice channel as the bot to do this.");
        this.runCMDVoice();
    }

    protected abstract void runCMDVoice();
}
