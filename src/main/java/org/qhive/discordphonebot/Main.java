package org.qhive.discordphonebot;

import org.qhive.discordphonebot.DiscordBot.BotWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		try {
			BotWrapper.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
