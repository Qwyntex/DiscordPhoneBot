package org.qhive.discordphonebot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.qhive.discordphonebot.DiscordBot.Commands.CommandManager;

import javax.security.auth.login.LoginException;

public class BotWrapper {

    public static void init() throws LoginException, InterruptedException {
        new BotWrapper();
    }
    public static JDA Bot;

    private BotWrapper() throws LoginException, InterruptedException {
        Bot = JDABuilder.createDefault(System.getenv("BOT-TOKEN"))
                .build().awaitReady();

        Bot.addEventListener(new CommandManager());
    }

    public static void sendMessage(String msg, User user) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        channel.sendMessage(msg).complete();
    }
}
