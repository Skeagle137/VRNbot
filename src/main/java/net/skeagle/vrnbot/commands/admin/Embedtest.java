package net.skeagle.vrnbot.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.skeagle.vrnbot.handlers.AdminCommand;

import java.awt.*;
import java.util.Random;

public class Embedtest extends AdminCommand {

    public Embedtest() {
        super("embedtest", "test", "");
    }

    @Override
    protected void runCMDAdmin() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Embed test", null);
        eb.setDescription("for reactions");
        Random r = new Random();
        eb.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        channel.sendMessage(eb.build()).queue(m -> {
            m.addReaction("👌").queue();
            m.addReaction("😎").queue();
            m.addReaction("✔").queue();
        });
    }
}
