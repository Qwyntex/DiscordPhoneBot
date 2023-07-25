package org.qhive.discordphonebot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.Nullable;
import org.qhive.discordphonebot.DiscordBot.Commands.CommandManager;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.qhive.discordphonebot.Util.log;

public class BotWrapper {
    public static User defaultUser;

    public static void init() throws LoginException, InterruptedException {
        Bot = JDABuilder.createDefault(System.getenv("BOT-TOKEN"))
                .build().awaitReady();

        Bot.addEventListener(new CommandManager());
        Bot.getGuilds().stream()
                .filter(guild -> {
                    if (guild.getMemberById("616967795913261057") == null) return false;
                    log(guild.getMemberById("616967795913261057").getId());
                    return guild.getMemberById("616967795913261057").getId() == "616967795913261057";
                })
                .findFirst()
                .ifPresent(guild -> defaultUser = guild.getMemberById("616967795913261057").getUser());
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
