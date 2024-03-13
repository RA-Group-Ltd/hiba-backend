package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;

public interface VerificationService {

    String generateUniqueCode();
    VerificationCode saveVerificationTokenForUser(User user, String token);
    VerificationCode createTokenForUser(User user);
    User findUserByVerificationToken(String token);

}
