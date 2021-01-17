package net.skeagle.vrnbot.commands.general;

import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.DeleteAuthorMsg;

@DeleteAuthorMsg
public class Say extends Command {
    public Say() {
        super("say");
        setDesc("Make the bot say a message.");
        setUsage("<message>");
        setCategory(Category.GENERAL);
    }

    @Override
    public void runCMD(){
        if (args.length < 1)
            returnsend("You must provide text after the command.");
        send(joinArgs(0));
    }
}
