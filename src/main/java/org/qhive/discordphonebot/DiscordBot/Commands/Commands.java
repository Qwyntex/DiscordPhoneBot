package org.qhive.discordphonebot.DiscordBot.Commands;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.qhive.discordphonebot.Database.Database;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;

import static org.qhive.discordphonebot.DiscordBot.Commands.CommandManager.registerSlashNewCommand;
public class Commands {

    private static final String noPermissionReply = "You do not seem to have permissions to execute this command.";
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
                    if (Database.getUserDbId(user.getId()) == -1) {
                        Database.addUser(user.getId());
                    }
                    String number = Database.getNextAvailableNumber();

                    if (number == null) {
                        event.reply("there are currently no more available numbers for you to register.")
                                .setEphemeral(true).queue();
                        return;
                    }

                    Database.assignNumber(number, user.getId());

                    user.openPrivateChannel().complete().sendMessage("You were registered for " + number).complete();
                    event.reply(
                            "You got registered the number " + number +
                                    "; look into your private messages for more information"
                    ).setEphemeral(true).queue();
                });

        registerSlashNewCommand(new Command(
                "update",
                "manually updates the bot",
                "/update"
                ),event -> {
                    if (event.getGuild() == null) {
                        event.reply("this is not a guild").queue();
                        return;
                    }
                    CommandManager.update(event.getGuild());
                    event.reply("commands were successfully updated").queue();
                });

        registerSlashNewCommand(new Command(
                "addnumber",
                "adds the specified number to the database",
                "/addnumber <number>",
                new OptionData(
                        OptionType.STRING,
                        "number",
                        "the number to be added to the database",
                        true
                        )
                ), event -> {
                    if (event.getMember() == null) return;
                    if (!hasAdminPrivileges(event.getMember())) {
                        event.reply(
                                noPermissionReply + "\nyou need the " + CommandManager.adminRole + " role")
                                .setEphemeral(true).queue();
                        return;
                    }


                    String number = event.getOption(
                            "number",
                            OptionMapping::getAsString
                    );
                    Database.addNumber(number);
                    event.reply("the number " + number + " has been added to the database")
                        .setEphemeral(true)
                        .queue();
                }
        );

        registerSlashNewCommand(new Command(
                "setadminchannel",
                "sets the channel to which errors will be printed DO NOT USE",
                "setadminchannel <channelname>",
                new OptionData(
                        OptionType.CHANNEL,
                        "channelname",
                        "the channel that will be used as the adminchannel",
                        true
                )), event -> {
                    // TODO: admin channel and log channel in database connected to guild
                    //  then set it here
                }
        );

        System.out.println("initialized commands");
    }

    private static boolean hasAdminPrivileges(Member member) {
        return true; // TODO: hmm, I wonder what might need some work here
    }
}
