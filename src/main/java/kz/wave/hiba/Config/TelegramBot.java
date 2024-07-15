package kz.wave.hiba.Config;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.VerificationCodeRepository;
import kz.wave.hiba.Service.AuthService;
import kz.wave.hiba.Service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String START = "/start";

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendVerificationCode(String chatId, String deepLink, String verificationCode) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Добро пожаловать! Ваш код подтверждения: " + verificationCode + ". Нажмите на ссылку для продолжения: " + deepLink);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace(); // Обработайте ошибку отправки сообщения
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();

            if(messageText.contains("/start")){
                    String username = update.getMessage().getChat().getUserName();
                    String phone = messageText.split("/start ")[1];
                    // Сохраните telegramChatId при первом подключении через deep link




                    startCommand(chatId, username, phone);
                }

        }
    }

    private void startCommand(Long chatId, String userName, String phone) {
        User user = userRepository.findByPhone(phone);

        if (user == null) {
            // Если пользователя нет, создаем нового и отправляем код
            user = authService.createUserWithPhoneNumber(phone);

            user.setTelegramChatId(chatId.toString());
            userRepository.save(user);
        }

        VerificationCode code = new VerificationCode();
        String verificationCode = String.format("%04d", new Random().nextInt(10000)); // генерирует 4-значный код
        code.setToken(verificationCode);
        // Сетим expirationDate, используя LocalDateTime
        code.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // код действителен 10 минут
        code.setUser(user);
        verificationCodeRepository.save(code);

        var text = " Добро пожаловать Hiba bot! " + code.getToken();

        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*public void generateAndSendVerificationCode(User user) {
        VerificationCode verificationCode = generateVerificationCode(user);
        verificationCodeRepository.save(verificationCode);
    }

    public VerificationCode generateVerificationCode(User user) {
        VerificationCode code = new VerificationCode();
        String verificationCode = String.format("%04d", new Random().nextInt(10000)); // генерирует 4-значный код
        code.setToken(verificationCode);
        // Сетим expirationDate, используя LocalDateTime
        code.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // код действителен 10 минут
        code.setUser(user);
        return code;
    }*/


}
