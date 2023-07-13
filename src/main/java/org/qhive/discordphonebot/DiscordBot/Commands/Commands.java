package org.qhive.discordphonebot.DiscordBot.Commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.qhive.discordphonebot.Database.Database;

import static org.qhive.discordphonebot.DiscordBot.Commands.CommandManager.registerSlashNewCommand;
public class Commands {
    public static void init() {
        System.out.println("initializing commands");

        registerSlashNewCommand(new Command(
                "register",
                "registers a number",
                "/register [number]",
                new OptionData(
                        OptionType.STRING,
                        "number",
                        "registers the specified number if accessible",
                        false
                )),
                event -> {
                    User user = event.getUser();
                    if (Database.getUserDbId(user.getId()) == null) {
                        Database.addUser(user.getId());
                    }
                    user.openPrivateChannel().complete().sendMessage("You were registered for {number}").complete();
                    event.reply(
                            "You got registered the number {number}," +
                                    " look into your private messages for more information"
                    ).setEphemeral(true).queue();
                });

        registerSlashNewCommand(new Command(
                "update",
                "updates the bots commands",
                "/update"
                ),event -> {
                    if (event.getGuild() == null) {
                        event.reply("this is not a guild").queue();
                        return;
                    }
                    CommandManager.updateCommands(event.getGuild());
                    event.reply("commands were successfully updated").queue();
                });


        System.out.println("initialized commands");
    }
}
