package net.skeagle.vrnbot.commands.general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.skeagle.vrnbot.handlers.Command;

import java.util.Random;

public class Coinflip extends Command {

    public Coinflip() {
        super("coinflip|coin");
        setDesc("Flips a coin.");
        setCategory(Category.GENERAL);
    }

    @Override
    protected void runCMD() {
        Random r = new Random();
        String s = (r.nextBoolean() ? "Heads" : "Tails");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(s);
        eb.setDescription(s + " was the result of the coin flip.");
        send(eb.build());
    }
}
