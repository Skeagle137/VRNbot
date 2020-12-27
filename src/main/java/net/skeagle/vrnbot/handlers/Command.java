package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.skeagle.vrnbot.handlers.exceptions.NoUserFoundException;
import net.skeagle.vrnbot.handlers.exceptions.VRNException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private String desc;
    private String usage;
    private List<String> aliases = new ArrayList<>();
    private String command;
    private Category category;
    protected JDA jda;
    protected String[] args;
    protected TextChannel channel;
    protected User author;
    protected Message msg;
    protected Guild g;
    protected GuildMessageReceivedEvent e;

    public enum Category {
        GENERAL("General"),
        MODERATION("Moderation"),
        MUSIC("Music & Voice"),
        SETTINGS("Settings"),
        ADMIN("Admin");

        private String category;

        Category(String category) {
            this.category = category;
        }

        public String obtainCategory() {
            return category;
        }
    }

    public final String getDescription() {
        return desc;
    }

    public boolean isAdmin() {
        return false;
    }

    public final String getCommand() {
        return command.toLowerCase();
    }

    public final String getUsage() {
        return usage;
    }

    public final List<String> getAliases() {
        return aliases;
    }

    public final String getCategory() {
        return category.obtainCategory();
    }

    private static String parseCommand(String command) {
        return command.split("\\|")[0];
    }

    private static List<String> parseAliases(String command) {
        String[] aliases = command.split("\\|");
        return (aliases.length > 0 ? Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length)) : new ArrayList<>());
    }

    protected void setDesc(String desc) {
        this.desc = desc == null ? "No description provided for this command." : desc;
    }

    protected void setUsage(String usage) {
        this.usage = usage == null ? "{prefix}" + command : "{prefix}" + command + " " + usage;
    }

    private void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    protected void setCategory(Category category) {
        this.category = category;
    }

    protected Command(String command) {
        this(parseCommand(command), parseAliases(command));
    }

    protected Command(String command, List<String> aliases) {
        this.setCommand(command);
        if (aliases != null) {
            this.setAliases(aliases);
        }
    }

    final void execute(String[] args, GuildMessageReceivedEvent e) {
        this.jda = e.getJDA();
        this.args = args;
        this.channel = e.getChannel();
        this.author = e.getAuthor();
        this.msg = e.getMessage();
        this.g = e.getGuild();
        this.e = e;

        try {
            runCMD();
        } catch (VRNException ex) {
            send(ex.getMessage());
        }
    }

    protected abstract void runCMD();

    private void setCommand(String command) {
        this.command = command;
    }

    protected boolean isInChannel(Guild g, User author) {
        Member member = g.getMember(author);
        return member.getVoiceState().getChannel() != null;
    }

    protected boolean joinVoice(Member member, Guild g) {
        AudioManager am = g.getAudioManager();
        isInChannel(g, member.getUser());
        if (am.isConnected() || isInChannel(g, member.getUser())) {
            am.openAudioConnection(member.getVoiceState().getChannel());
            return true;
        }
        send("You must be in a voice channel to do this.");
        return false;
    }

    protected void sendDM(User author, String... msg) {
        author.openPrivateChannel().queue((channel1) -> {
            for (String message : msg) {
                channel1.sendMessage(message).queue();
            }
        });
    }

    protected void sendDMEmbed(User author, MessageEmbed embed) {
        author.openPrivateChannel().queue((channel1) -> channel1.sendMessage(embed).queue());
    }

    protected void send(String msg) {
        channel.sendMessage(msg).queue();
    }

    protected void send(MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }

    protected boolean isInSameChannel(Guild g, Member member) {
        AudioManager am = g.getAudioManager();
        if (am.getConnectedChannel() == null || !member.getVoiceState().inVoiceChannel()) return false;
        return am.getConnectedChannel().getId().equals(member.getVoiceState().getChannel().getId());
    }

    protected User getUser(int index) {
        if (!msg.getMentionedUsers().isEmpty())
            return msg.getMentionedUsers().get(0);
        String id = args[index];

        User user = null;
        try {
            user = g.getJDA().retrieveUserById(id).complete();
        } catch (Exception ignored) {}

        if (user == null) throw new NoUserFoundException();
        return user;
    }

    protected Member getMember(final int index) {
        if (!msg.getMentionedMembers().isEmpty())
            return msg.getMentionedMembers().get(0);
        String id = args[index];

        Member member = null;
        try {
            member = g.getMemberById(id);
        } catch (Exception ignored) {}

        if (member == null) throw new NoUserFoundException();
        return member;
    }

    protected String joinArgs(int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
}
