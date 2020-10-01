package net.skeagle.vrnbot.handlers.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;
    private final AudioPlayerManager apm;
    private AudioPlayer player;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.apm = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(apm);
        AudioSourceManagers.registerLocalSource(apm);
        player = apm.createPlayer();
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(apm);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        apm.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                if (musicManager.player.getPlayingTrack() != null) {
                    musicManager.scheduler.queue(track);
                    channel.sendMessage("Adding to queue **" + track.getInfo().title + "**").queue();
                    return;
                }

                play(musicManager, track);
                channel.sendMessage("Now playing: **" + track.getInfo().title + "**").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                String msg = "Now playing: ";

                if (musicManager.player.getPlayingTrack() != null) {
                    msg = "Adding to queue: ";
                }

                channel.sendMessage(msg + "**" + firstTrack.getInfo().title + "** (first track of playlist **" + playlist.getName() + "**)").queue();

                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing was found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    /*
    public void play(TextChannel channel, String track) {
        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        apm.loadItem(track, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                trackScheduler.queue(track);
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    trackScheduler.queue(track);
                    player.playTrack(track);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("No matches were found for " + track).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Error: " + exception.getMessage()).queue();
            }
        });
    }*/
}
