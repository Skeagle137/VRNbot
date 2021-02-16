package net.skeagle.vrnbot.commands.setting;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.skeagle.vrnbot.handlers.BotPerms;
import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.Perms;
import net.skeagle.vrnbot.settings.guildsettings.GuildSettings;

import java.util.ArrayList;
import java.util.List;

@BotPerms(perms = {Permission.MANAGE_SERVER, Permission.MANAGE_ROLES})
@Perms(perms = {Permission.MANAGE_SERVER, Permission.MANAGE_ROLES})
public class DefaultRoles extends Command {

    public DefaultRoles() {
        super("defaultroles");
        setDesc("Manage default roles that a user will be assigned to upon joining the discord server.");
        setUsage("<set [list of roles] | clear>");
        setCategory(Category.SETTINGS);
    }

    @Override
    public void runCMD() {
        if (args.length < 1) {
            List<String> names = new ArrayList<>();
            for (String role : GuildSettings.getInstance().getSettings(g).getDefaultroles()) {
                Role r = g.getRoleById(role);
                if (r != null)
                    names.add(r.getName());
            }
            if (names.isEmpty())
                returnsend("There are no default roles for this server.");
            send("The default roles are: " + String.join(", ", names));
        }

        else if ("set".equalsIgnoreCase(args[0])) {
            if (args.length < 2)
                returnsend("You must provide one or more role names or mentions.");
            else {
                List<String> list = new ArrayList<>();
                for (int i = 1; i < args.length; i++)
                    list.add(getRole(i).getId());

                GuildSettings.getInstance().getSettings(g).setDefaultroles(list);
                send("New default role(s) set.");
            }
        }

        else if ("clear".equalsIgnoreCase(args[0])) {
            GuildSettings.getInstance().getSettings(g).setDefaultroles(new ArrayList<>());
            send("Default role(s) reset.");
        }
    }
}
