package org.qhive.discordphonebot.Webhook;

import org.qhive.discordphonebot.Database.Temp;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    @PostMapping("/webhook")
    public void receiveWebhook(@RequestBody String payload) {
        System.out.println(payload);
        BotWrapper.sendMessage(payload, Temp.getUserFromNumber());
    }
}
