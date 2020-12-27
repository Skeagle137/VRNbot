package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.skeagle.vrnbot.handlers.lavaplayer.GuildMusicManager;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;
import net.skeagle.vrnbot.settings.Prefix;
import net.skeagle.vrnbot.utils.GuildMusicCache;

public class Listeners extends ListenerAdapter {

    /*
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (!Bot.lite) {
            //e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRolesByName("default", true).get(0)).queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Welcome!", null);
            Random r = new Random();
            eb.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
            eb.setDescription("Welcome **" + e.getUser().getName() + "** to the " + e.getGuild().getName() + " discord server !");
            eb.addField("Information", "The VRN Group is a cool place. Hope you enjoy. In addition, you can also try out using VRN-bot!" + e.getUser().getAvatarUrl(), false);
            eb.setFooter("\"© VRN Discord Server 2017 - 2020\"", e.getJDA().getSelfUser().getAvatarUrl());
            eb.setThumbnail(e.getUser().getAvatarUrl());
            try {
                e.getGuild().getDefaultChannel().sendMessage(eb.build()).queue();
            } catch (NullPointerException ignored) {}
        }
    }

     */

    /*
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        if (!Bot.lite) {
            try {
                e.getGuild().getDefaultChannel().sendMessage("**" + e.getUser().getName() + "** has left the VRN Group. Sad to see them go. :frowning:").queue();
            } catch (NullPointerException ignored) {}
        }
    }

     */

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
        if (bot == null) return;
        if (!bot.getVoiceState().inVoiceChannel()) return;
        if (e.getMember().getUser() != e.getJDA().getSelfUser())
            if (e.getChannelLeft().getMembers().size() < 2 && e.getChannelLeft().getMembers().contains(bot)) {
                GuildMusicCache.getInstance().getVolumeCache().remove(e.getGuild().getId());
                PlayerManager playerManager = PlayerManager.getInstance();
                GuildMusicManager musicManager = playerManager.getGuildMusicManager(e.getGuild());
                if (musicManager.player.getPlayingTrack() != null && !musicManager.player.isPaused())
                    musicManager.player.stopTrack();
                e.getGuild().getAudioManager().closeAudioConnection();
            }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        if (e.getMember().getUser() != e.getJDA().getSelfUser()) return;
        System.out.println(e.getChannelJoined().getMembers().size());
        if (e.getChannelJoined().getMembers().size() != 1) return;
        GuildMusicCache.getInstance().getVolumeCache().remove(e.getGuild().getId());
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
        String prefix = Prefix.getInstance().getPrefixes().computeIfAbsent(e.getGuild().getIdLong(), Prefix.getInstance()::get);
        if (e.getMessage().getMentionedUsers().contains(botmention)) {
            e.getChannel().sendMessage("My prefix here is `" + prefix + "`. If you need any help, do `" + prefix + "help`.").queue();
            return;
        }
        if (rawmsg.equalsIgnoreCase(prefix + "key")) {
            e.getAuthor().openPrivateChannel().queue((channel1) ->
                    channel1.sendMessage("oh. you figured it out. well, the password, or key, is 1357908642.").queue());
            return;
        }
        if (rawmsg.toLowerCase().startsWith(prefix)) {
            CommandHandler handle = new CommandHandler();
            handle.doCommand(e, prefix);
        }
    }
}
