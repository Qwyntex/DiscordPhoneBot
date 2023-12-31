package org.qhive.discordphonebot.Webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import org.apache.tomcat.util.json.JSONFilter;
import org.qhive.discordphonebot.Database.Database;
import org.qhive.discordphonebot.DiscordBot.BotWrapper;
import org.qhive.discordphonebot.Util;
import org.qhive.discordphonebot.VOIP.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class WebhookController {
    @PostMapping("/dpbot/messages")
    public void receiveWebhook(@RequestBody String payload) {
        Util.log(payload);

        ObjectMapper objectMapper = new ObjectMapper();

        Message message;

        try {
            message = objectMapper.readValue(payload, Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e + "\n could not process JSON payload");
        }

        String number = message.to;
        String user_id = Database.getUserFromNumber(number);
        if (user_id == null) return;
        User user = BotWrapper.getUserFromID(user_id);
        if (user == null) return;

        String reply = message.getAsReadableMessage();

        BotWrapper.sendMessage(reply, user);
    }
}
