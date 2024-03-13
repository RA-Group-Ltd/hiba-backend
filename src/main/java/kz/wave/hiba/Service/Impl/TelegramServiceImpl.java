package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Service.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramServiceImpl implements TelegramService {

    private final RestTemplate restTemplate;
    private final String telegramBotToken = "6437904956:AAGM1GY9sr_lTKN4Qt6BVdKVyWwfnBMYlWw";
    private final String telegramBotUrl = "https://api.telegram.org/bot";

    public TelegramServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void sendCode(String chatId, String code) {
        String url = telegramBotUrl + telegramBotToken + "/sendMessage?chat_id=" + chatId + "&text=Ваш код подтверждения: " + code;
        restTemplate.getForObject(url, String.class);
    }
}
