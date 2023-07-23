package org.qhive.discordphonebot.Webhook;

import net.dv8tion.jda.api.entities.User;
import org.qhive.discordphonebot.Database.Database;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;
import org.qhive.discordphonebot.Util;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    @PostMapping("/webhook")
    public void receiveWebhook(@RequestBody String payload) {
        Util.log(payload);
        String number = "1234567";
        String user_id = Database.getUserFromNumber(number);
        if (user_id == null) return;
        User user = BotWrapper.getUserFromID(user_id);
        if (user == null) {
            Util.log("User is null, no user found");
            return;
        }
        try {
            BotWrapper.sendMessage(payload, user);
        } catch (Exception e) {
            throw new RuntimeException(e + "failed to send message");
        }
        // TODO: so much
    }
}
