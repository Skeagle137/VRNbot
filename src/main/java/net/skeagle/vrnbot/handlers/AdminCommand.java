package net.skeagle.vrnbot.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.skeagle.vrnbot.utils.Config;

public abstract class AdminCommand extends Command {

    protected JDA jda;
    protected String[] args;
    protected TextChannel channel;
    protected User author;
    protected Message msg;
    protected Guild g;
    protected GuildMessageReceivedEvent e;

    public AdminCommand(String name, String desc, String usage) {
        super(name);
        setDesc(desc);
        setUsage(usage);
        setCategory(Category.ADMIN);
    }

    public AdminCommand(String name, String desc) {
        super(name);
        setDesc(desc);
        setCategory(Category.ADMIN);
    }

    @Override
    protected final void runCMD() {
        this.jda = super.jda;
        this.args = super.args;
        this.channel = super.channel;
        this.author = super.author;
        this.msg = super.msg;
        this.g = super.g;
        this.e = super.e;

        if (!author.getId().equals(Config.OWNERID)) return;
        runCMDAdmin();
    }

    @Override
    public final boolean isAdmin() {
        return true;
    }

    protected abstract void runCMDAdmin();
}
