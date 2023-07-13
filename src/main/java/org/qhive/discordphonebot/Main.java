package org.qhive.discordphonebot;

import org.qhive.discordphonebot.Database.Database;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		// startup db
		try {
			Database.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// startup spring
		SpringApplication.run(Main.class, args);

		// startup discord bot
		try {
			BotWrapper.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
