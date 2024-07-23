package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Service.TelegramService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of the {@link TelegramService} interface for sending messages via Telegram bot.
 */
@Service
public class TelegramServiceImpl implements TelegramService {

    private final RestTemplate restTemplate;
    private final String telegramBotToken = "6437904956:AAGM1GY9sr_lTKN4Qt6BVdKVyWwfnBMYlWw";
    private final String telegramBotUrl = "https://api.telegram.org/bot";

    /**
     * Constructs a new {@code TelegramServiceImpl} with the provided {@link RestTemplateBuilder}.
     *
     * @param restTemplateBuilder the RestTemplateBuilder used to create a RestTemplate instance
     */
    public TelegramServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Sends a confirmation code to a specified Telegram chat.
     *
     * @param chatId the ID of the Telegram chat
     * @param code the confirmation code to be sent
     */
    @Override
    public void sendCode(String chatId, String code) {
        String url = telegramBotUrl + telegramBotToken + "/sendMessage?chat_id=" + chatId + "&text=Ваш код подтверждения: " + code;
        restTemplate.getForObject(url, String.class);
    }
}
