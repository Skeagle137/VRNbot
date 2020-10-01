package net.skeagle.vrnbot.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.skeagle.vrnbot.handlers.Command;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Purge extends Command {
    public Purge() {
        super("purge");
        setDesc("Deletes a specified amount of the most recent messages (max of 50).");
        setUsage("<amount>");
        setCategory(Category.MODERATION);
    }

    @Override
    public void runCMD() {
        if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You do not have the `Manage Messages` permission required to use this command.").queue();
            return;
        }

        if (!g.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE)) {
            send("I need the `Manage Messages` permission for this command.");
            return;
        }

        if (args.length < 1) {
            send("You must provide the amount of messages to delete after the command.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
            send("That is not a valid number.");
            return;
        }

        if (amount < 2 || amount > 100) {
            send("The amount of messages must be at least 2 and no more than 100.");
            return;
        }

        channel.getIterableHistory().takeAsync(amount + 1).thenApplyAsync((msgs) -> {
            List<Message> msgList = msgs.stream().filter((m) -> !m.getTimeCreated()
                    .isAfter(OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
                    ))
                    .collect(Collectors.toList());
            channel.purgeMessages(msgList);
            return msgList.size();
        }).whenCompleteAsync(
                (count, thr) -> channel.sendMessageFormat("Successfully purged `" + (count - 1) + "` messages.").queue(
                        (message -> message.delete().queueAfter(6, TimeUnit.SECONDS)))
        ).exceptionally((thr) -> {
            String exception = "";

            if (thr.getCause() != null) {
                exception = " caused by: " + thr.getCause().getMessage();
            }
            channel.sendMessageFormat("Exception: " + thr.getMessage() + exception).queue();
            return 0;
        });


    }
}
