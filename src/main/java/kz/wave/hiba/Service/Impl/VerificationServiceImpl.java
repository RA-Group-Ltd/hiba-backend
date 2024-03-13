package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;
import kz.wave.hiba.Repository.VerificationTokenRepository;
import kz.wave.hiba.Service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationTokenRepository tokenRepository;

    public String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public VerificationCode saveVerificationTokenForUser(User user, String token) {
        VerificationCode verificationToken = new VerificationCode();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // Устанавливаем срок действия токена
        return tokenRepository.save(verificationToken);
    }

    public VerificationCode createTokenForUser(User user) {
        String token = UUID.randomUUID().toString(); // Пример генерации уникального токена
        VerificationCode verificationToken = new VerificationCode(token, user);
        return tokenRepository.save(verificationToken);
    }

    public User findUserByVerificationToken(String token) {
        VerificationCode verificationToken = tokenRepository.findByToken(token);
        return verificationToken != null ? verificationToken.getUser() : null;
    }
}
