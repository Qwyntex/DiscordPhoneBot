package org.qhive.discordphonebot.VOIP;

public class Message {
    /*
    example message:

    {
      "from": "+18445550001",
      "messaging_profile_id": "abc85f64-5717-4562-b3fc-2c9600000000",
      "to": "+18445550001",
      "text": "Hello, World!",
      "subject": "From Telnyx!",
      "media_urls": [
        "http://example.com"
      ],
      "webhook_url": "http://example.com/webhooks",
      "webhook_failover_url": "https://backup.example.com/hooks",
      "use_profile_webhooks": true,
      "type": "MMS"
    }
    */


    public Message() {}

    public String
            from,
            messaging_profile_id,
            to,
            text,
            subject,
            webhook_url,
            webhook_failover_url,
            type;

    public String[] media_urls;
    public boolean use_profile_webhooks;

    public String getAsReadableMessage(boolean textOnly) {
        return textOnly ? String.format("""
                `To:   %s`
                `From: %s`
                ### %s
                > %s
                """,
                to,
                from,
                subject,
                text
        ) : String.format("""
                > %s
                """,
                text
        );
    }
}
