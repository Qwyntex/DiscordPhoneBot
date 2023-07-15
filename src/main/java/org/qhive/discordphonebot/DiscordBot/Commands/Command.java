package org.qhive.discordphonebot.DiscordBot.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Command {
    public String name, description, usage;
    public OptionData[] options;
    private Consumer<SlashCommandInteractionEvent> action;

    public Command(String name, String description, String usage, OptionData... options) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.options = options;
    }

    public void setAction(Consumer<SlashCommandInteractionEvent> action) {
        this.action = action;
    }

    public void execute(SlashCommandInteractionEvent event) {
        this.action.accept(event);
    }
}
