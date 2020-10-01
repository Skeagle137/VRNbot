package net.skeagle.vrnbot.commands.general;

import net.dv8tion.jda.api.Permission;
import net.skeagle.vrnbot.handlers.Command;

public class Say extends Command {
    public Say() {
        super("say");
        setDesc("Make the bot say a message.");
        setUsage("<message>");
        setCategory(Category.GENERAL);
    }

    @Override
    public void runCMD(){
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (String arg : args) {
                sb.append(arg).append(" ");
            }

            if (g.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE)) {
                msg.delete().queue();
            }
            send(sb.toString());
            return;
        }
            sendDM(author,"You must provide text after the command.");
    }
}
