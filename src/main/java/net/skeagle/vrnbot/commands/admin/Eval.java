package net.skeagle.vrnbot.commands.admin;

import net.skeagle.vrnbot.handlers.AdminCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.Executors;

public class Eval extends AdminCommand {

    public Eval() {
        super("eval|evaluate",
                "Evaluate code and send it to the channel.",
                "<script>");
    }

    @Override
    public void runCMDAdmin() {
        if (args.length > 0) {
            Executors.newCachedThreadPool().execute(() -> {
                try {
                    ScriptEngine script = new ScriptEngineManager().getEngineByExtension("js");
                    script.put("jda", jda);
                    script.put("channel", channel);
                    script.put("author", author);
                    script.put("msg", msg);
                    script.put("args", args);
                    script.put("g", g);
                    Object o;
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    o = script.eval(sb.toString());
                    if (o != null) {
                        channel.sendMessage("Output: " + o).queue();
                    }
                } catch (ScriptException e) {
                    channel.sendMessage("An error occurred:\n" + e.getMessage()).queue();
                    e.printStackTrace();
                }
            });
            return;
        }
        send("You must provide code after the command.");
    }
}
