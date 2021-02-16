package net.skeagle.vrnbot.commands.setting;

import net.dv8tion.jda.api.Permission;
import net.skeagle.vrnbot.handlers.BotPerms;
import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.Perms;
import net.skeagle.vrnbot.settings.guildsettings.GuildSettings;

@BotPerms(perms = {Permission.MANAGE_SERVER})
@Perms(perms = {Permission.MANAGE_SERVER})
public class SetPrefix extends Command {

    public SetPrefix() {
        super("setprefix|prefix");
        setDesc("Set the guild prefix of the bot.");
        setUsage("<prefix>");
        setCategory(Category.SETTINGS);
    }

    @Override
    public void runCMD() {
        if (args.length < 1)
            returnsend("You must provide a new prefix.");

        if (args[0].length() > 3)
            returnsend("Prefix must be no greater than 3 characters.");

        GuildSettings.getInstance().getSettings(g).setPrefix(args[0]);
        send("Set the prefix to `" + args[0] + "`.");
    }
}
