package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;
import kz.wave.hiba.Repository.VerificationTokenRepository;
import kz.wave.hiba.Service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the {@link VerificationService} interface.
 */
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationTokenRepository tokenRepository;

    /**
     * Generates a unique verification code.
     *
     * @return a unique 6-character verification code
     */
    public String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * Saves a verification token for the given user.
     *
     * @param user the user for whom the token is generated
     * @param token the verification token
     * @return the saved verification token entity
     */
    public VerificationCode saveVerificationTokenForUser(User user, String token) {
        VerificationCode verificationToken = new VerificationCode();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // Set the expiration date of the token
        return tokenRepository.save(verificationToken);
    }

    /**
     * Creates a verification token for the given user.
     *
     * @param user the user for whom the token is created
     * @return the saved verification token entity
     */
    public VerificationCode createTokenForUser(User user) {
        String token = UUID.randomUUID().toString(); // Example of generating a unique token
        VerificationCode verificationToken = new VerificationCode(token, user);
        return tokenRepository.save(verificationToken);
    }

    /**
     * Finds a user by the given verification token.
     *
     * @param token the verification token
     * @return the user associated with the token, or null if not found
     */
    public User findUserByVerificationToken(String token) {
        VerificationCode verificationToken = tokenRepository.findByToken(token);
        return verificationToken != null ? verificationToken.getUser() : null;
    }
}
