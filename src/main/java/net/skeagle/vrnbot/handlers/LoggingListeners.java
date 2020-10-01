package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LoggingListeners extends ListenerAdapter {

    /*
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        TextChannel log = e.getJDA().getTextChannelById(getChannel(e.getGuild().getIdLong()));
        if (log == null) return;
        if (e.getChannel() == log || e.getAuthor().isBot()) return;
        Message after = e.getChannel().retrieveMessageById(e.getMessageId()).complete();
        EmbedBuilder embed = new EmbedBuilder().setColor(new Color(255, 230, 0))
                .addField(e.getAuthor().getAsTag() + " (" + e.getAuthor().getId() + ")",
                        "Message " + e.getMessage().getId() + " edited in " + e.getChannel().getAsMention() +
                                "\n**Before:** " + e.getMessage().getContentRaw() +
                                "\n**After:** " + after.getContentRaw(), false);
        if (!e.getMessage().getAttachments().isEmpty()) {
            embed.setImage(e.getMessage().getAttachments().get(0).getUrl());
            StringBuilder urls = new StringBuilder();
            for (Message.Attachment file : e.getMessage().getAttachments()) {
                urls.append(file.getUrl()).append("\n");
            }
            embed.addField("**Attatchments**", urls.toString(), false);
        }
        embed.setTimestamp(new Date().toInstant());
        log.sendMessage(embed.build()).queue();
    }

    private String getChannel(long guildId) {
        return LogChannel.getInstance().getChannels().computeIfAbsent(guildId, LogChannel.getInstance()::get);
    }

     */
}
