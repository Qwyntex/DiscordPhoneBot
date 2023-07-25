package org.qhive.discordphonebot.DiscordBot.Commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class CommandManager extends ListenerAdapter {
    private static final List<Command> commands = new ArrayList<>();
    public static final String adminRole = "DPBot Manager";

    public CommandManager() {
        org.qhive.discordphonebot.DiscordBot.Commands.Commands.init();
        BotWrapper.Bot.getGuilds()
                .forEach(CommandManager::update);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        commands.stream()
                .filter(command -> event.getName().equals(command.name))
                .findFirst().ifPresent(command -> command.execute(event));
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        update(event.getGuild());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        update(event.getGuild());
    }

    @Override
    public void onGuildAvailable(@NotNull GuildAvailableEvent event) {
        update(event.getGuild());
    }



    public static void update(Guild guild) {
        // Roles

//        guild.createRole()
//                .setName(adminRole)
//                .setColor(Color.CYAN)
//                .queue();


        // Commands

        Collection<CommandData> commandData = new ArrayList<>();
        commands.forEach(command -> commandData.add(
                Commands.slash(command.name, command.description).addOptions(command.options)
        ));
        guild.updateCommands().addCommands(commandData).queue();
    }

    public static void registerSlashNewCommand(Command command, Consumer<SlashCommandInteractionEvent> action) {
        command.setAction(action);
        commands.add(command);
    }

    // TODO: create a nice way to easily add interact-able messages
    // TODO: create a nice way to handle permissions
}
