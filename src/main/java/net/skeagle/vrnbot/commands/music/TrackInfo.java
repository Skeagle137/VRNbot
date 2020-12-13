package net.skeagle.vrnbot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.skeagle.vrnbot.handlers.VoiceCommand;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

import java.awt.*;

import static net.skeagle.vrnbot.utils.TimeUtil.timeToMessage;

public class TrackInfo extends VoiceCommand {

    public TrackInfo() {
        super("trackinfo|songinfo|tinfo", false, true, false);
        setDesc("Gives more information on the currently playing track.");
    }

    @Override
    public void runCMDVoice() {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(g);
        if (musicManager.player.getPlayingTrack() == null) {
            send("There is no currently playing track to get information on.");
            return;
        }
        AudioTrackInfo info = musicManager.player.getPlayingTrack().getInfo();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**" + info.title + "**");
        eb.addField("**Author:**", info.author, false);
        eb.addField("**Track Length:**", timeToMessage(info.length / 1000), false);
        eb.addField("**Track URL:**", String.valueOf(info.uri), false);
        eb.addField("**Track is a stream:**", (info.isStream ? "Yes" : "No"), false);
        eb.setColor(Color.cyan);
        channel.sendMessage(eb.build()).queue();
    }

}
