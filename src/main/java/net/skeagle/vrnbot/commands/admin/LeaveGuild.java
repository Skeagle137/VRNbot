package net.skeagle.vrnbot.commands.admin;

import net.skeagle.vrnbot.handlers.AdminCommand;

public class LeaveGuild extends AdminCommand {

    public LeaveGuild() {
        super("leaveguild","Forces the bot to leave the guild.");
    }

    @Override
    protected void runCMDAdmin() {
        g.leave().queue();
    }
}
