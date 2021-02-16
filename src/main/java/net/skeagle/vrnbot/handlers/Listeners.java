package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;
import net.skeagle.vrnbot.settings.guildsettings.GuildSettings;
import net.skeagle.vrnbot.settings.guildsettings.Settings;
import net.skeagle.vrnbot.utils.GuildCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Listeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        Settings s = GuildSettings.getInstance().getSettings(e.getGuild());
        Role r;
        List<String> updated = new ArrayList<>();
        for (String role : s.getDefaultroles()) {
            r = e.getGuild().getRoleById(role);
            if (r == null) continue;
            updated.add(role);
            e.getGuild().addRoleToMember(e.getMember(), r).queue();
        }
        if (updated.size() != s.getDefaultroles().size())
            s.setDefaultroles(updated);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
        if (bot == null) return;
        if (!bot.getVoiceState().inVoiceChannel()) return;
        if (e.getMember().getUser() != e.getJDA().getSelfUser())
            if (e.getChannelLeft().getMembers().size() < 2 && e.getChannelLeft().getMembers().contains(bot)) {
                GuildCache.getInstance().getVolumeCache().remove(e.getGuild().getId());
                PlayerManager playerManager = PlayerManager.getInstance();
                GuildMusicManager musicManager = playerManager.getGuildMusicManager(e.getGuild());
                if (musicManager.player.getPlayingTrack() != null && !musicManager.player.isPaused())
                    musicManager.player.stopTrack();
                e.getGuild().getAudioManager().closeAudioConnection();
            }
    }

    @Override
    public void onGuildVoiceMute(GuildVoiceMuteEvent e) {
        if (e.getMember().getUser() != e.getJDA().getSelfUser()) return;
        Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
        if (bot == null) return;
        if (bot.getVoiceState().isGuildMuted() && bot.hasPermission(Permission.VOICE_MUTE_OTHERS))
            bot.mute(false).queue();
    }

    @Override
    public void onGuildVoiceDeafen(GuildVoiceDeafenEvent e) {
        if (e.getMember().getUser() != e.getJDA().getSelfUser()) return;
        Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
        if (bot == null) return;
        if (bot.getVoiceState().isGuildDeafened() && bot.hasPermission(Permission.VOICE_DEAF_OTHERS))
            bot.deafen(false).queue();
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        if (e.getMember().getUser() == e.getJDA().getSelfUser() && e.getChannelJoined().getMembers().size() != 1) return;
        else if (e.getMember().getUser() != e.getJDA().getSelfUser() && e.getChannelLeft().getMembers().size() != 1) return;
        GuildCache.getInstance().getVolumeCache().remove(e.getGuild().getId());
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(e.getGuild());
        if (musicManager.player.getPlayingTrack() != null && !musicManager.player.isPaused())
            musicManager.player.stopTrack();
        e.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String rawmsg = e.getMessage().getContentRaw();
        User botmention = e.getJDA().getSelfUser();
        String prefix = GuildSettings.getInstance().getSettings(e.getGuild()).getPrefix();
        if (e.getMessage().getMentionedUsers().contains(botmention)) {
            e.getChannel().sendMessage("My prefix here is `" + prefix + "`. If you need any help, do `" + prefix + "help`.").queue();
            return;
        }
        if (rawmsg.toLowerCase().startsWith(prefix.toLowerCase())) {
            CommandHandler handle = new CommandHandler();
            handle.doCommand(e, prefix);
        }
    }
}
