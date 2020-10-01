package net.skeagle.vrnbot.commands.moderation;

import net.skeagle.vrnbot.handlers.Command;

public class Mute extends Command {

    public Mute() {
        super("mute");
        setDesc("Mute a user. If mute role is not specified, it will be created.");
        setUsage("<user> [reason] [-s]");
        setCategory(Category.MODERATION);
    }

    @Override
    public void runCMD() {
        if (args.length < 1) {

        }
    }

}
