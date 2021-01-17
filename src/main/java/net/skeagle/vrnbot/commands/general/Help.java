package net.skeagle.vrnbot.commands.general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.skeagle.vrnbot.utils.Config;
import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.CommandHandler;
import net.skeagle.vrnbot.settings.Prefix;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Help extends Command {
    private final CommandHandler handler;

    public Help(CommandHandler handler) {
        super("help|h|?");
        setDesc("Provides help and the list of commands.");
        setUsage("[command]");
        this.handler = handler;
        setCategory(Category.GENERAL);
    }

    @Override
    public void runCMD() {
        if (args.length < 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("__List of commands__\n\n");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Command Help Information", null);
            Random r = new Random();
            eb.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
            eb.setDescription(sb);
            List<String> categorized = new ArrayList<>();
            handler.getCommands().stream().map(Command::getCommand).forEach(categorized::add);
            for (Category category : Category.values())
                if (category != Category.ADMIN)
                    eb.addField("***" + category.obtainCategory() + "***", "`" + String.join("`, `", sorted(category, categorized)) + "`", false);
            channel.sendMessage(eb.build()).queue();
            return;
        }

        Command base = handler.getCmd(args[0]);
        if (base == null || (base.isAdmin() && !author.getId().equals(Config.OWNERID)))
            returnsend("Nothing found for " + args[0]);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Command Help Information - " + base.getCommand(), null);
        Random r = new Random();
        eb.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        eb.addField("Description", base.getDescription(), false);
        eb.addField("Usage", base.getUsage().replaceAll("\\{prefix}",
                Prefix.getInstance().getPrefixes().computeIfAbsent(g.getIdLong(), Prefix.getInstance()::get)), false);
        eb.addField("Aliases", base.getAliases().isEmpty() ? "(None)" : String.join(", ", base.getAliases()), false);
        eb.addField("Category", base.getCategory(), false);
        channel.sendMessage(eb.build()).queue();
    }

    private List<String> sorted(Category category, List<String> commands) {
        List<String> categorized = new ArrayList<>();
        for (String cmd : commands) {
            Command base = handler.getCmd(cmd);
            if (base.getCategory().equalsIgnoreCase(category.obtainCategory())) {
                if (base.isAdmin())
                    break;
                categorized.add(base.getCommand());
            }
        }
        return categorized;
    }
}
