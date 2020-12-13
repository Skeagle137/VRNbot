package net.skeagle.vrnbot.handlers;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.skeagle.vrnbot.commands.admin.BlackList;
import net.skeagle.vrnbot.commands.admin.Eval;
import net.skeagle.vrnbot.commands.admin.Setavatar;
import net.skeagle.vrnbot.commands.general.*;
import net.skeagle.vrnbot.commands.moderation.Kick;
import net.skeagle.vrnbot.commands.moderation.Purge;
import net.skeagle.vrnbot.commands.music.*;
import net.skeagle.vrnbot.commands.setting.Setprefix;
import net.skeagle.vrnbot.commands.general.Help;
import net.skeagle.vrnbot.settings.BlacklistedUsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandHandler {

    @Getter
    private final List<Command> commands = new ArrayList<>();

    CommandHandler() {
        addCmd(new Eval());
        addCmd(new Help(this));
        addCmd(new Purge());
        addCmd(new Say());
        addCmd(new Setavatar());
        addCmd(new Join());
        addCmd(new Leave());
        addCmd(new Play());
        addCmd(new Stop());
        addCmd(new Volume());
        addCmd(new Setprefix());
        addCmd(new BlackList());
        addCmd(new Kick());
        addCmd(new Pause());
        addCmd(new Coinflip());
        addCmd(new Unpause());
        addCmd(new TrackInfo());
    }

    private void addCmd(Command base) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getCommand().equalsIgnoreCase(base.getCommand()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present.");
        }

        commands.add(base);
    }

    public Command getCmd(String search) {
        for (Command base : commands) {
            if (base.getCommand().equals(search.toLowerCase()) || base.getAliases().contains(search.toLowerCase())) {
                return base;
            }
        }
        return null;
    }

    void doCommand(GuildMessageReceivedEvent e, String prefix) {
        if (e.getAuthor().isBot()) return;

        if (BlacklistedUsers.getInstance().isBlacklisted(e.getAuthor().getId())) return;

        String[] splitCmd = e.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        String justCmdNoArgs = splitCmd[0].toLowerCase();
        Command base = getCmd(justCmdNoArgs);
        if (base != null) {
            String[] args = Arrays.copyOfRange(splitCmd, 1, splitCmd.length);
            base.execute(args, e);
        }
    }
}
