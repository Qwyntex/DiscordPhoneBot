package org.qhive.discordphonebot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.Nullable;
import org.qhive.discordphonebot.DiscordBot.Commands.CommandManager;
import org.qhive.discordphonebot.Util;

import javax.security.auth.login.LoginException;

import static org.qhive.discordphonebot.Main.dotenv;

public class BotWrapper {

    public static void init() throws LoginException, InterruptedException {
        Bot = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"))
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
        User user = Bot.retrieveUserById(user_id).complete();
        if (user == null) Util.log("could not retrieve user with that id");
        return user;
    }
}
