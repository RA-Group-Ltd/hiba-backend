package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link VerificationCode} entities related to verification tokens.
 */
@Repository
@Transactional
public interface VerificationTokenRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Finds a verification code by its token.
     *
     * @param token the verification token
     * @return the verification code associated with the specified token
     */
    VerificationCode findByToken(String token);
}
