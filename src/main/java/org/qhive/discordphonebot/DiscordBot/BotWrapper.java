package org.qhive.discordphonebot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.Nullable;
import org.qhive.discordphonebot.DiscordBot.Commands.CommandManager;

import javax.security.auth.login.LoginException;

public class BotWrapper {

    public static void init() throws LoginException, InterruptedException {
        Bot = JDABuilder.createDefault(System.getenv("BOT-TOKEN"))
                .build().awaitReady();

        Bot.addEventListener(new CommandManager());
    }
    public static JDA Bot;

    public static void sendMessage(String msg, User user) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        channel.sendMessage(msg).complete();
    }

    public static void sendMessage(String msg, GuildMessageChannel channel) {
        channel.sendMessage(msg).complete();
    }

    @Nullable
    public static User getUserFromID(String user_id) {
        return Bot.retrieveUserById(user_id).complete();
    }
}
