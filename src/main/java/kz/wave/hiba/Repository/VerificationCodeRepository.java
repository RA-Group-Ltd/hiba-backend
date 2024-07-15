package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for {@link VerificationCode} entities.
 */
@Repository
@Transactional
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Finds verification codes by user ID and expiration date after the specified date and time.
     *
     * @param id the user ID
     * @param now the date and time to compare with the expiration date
     * @return a list of verification codes matching the criteria
     */
    List<VerificationCode> findByUserIdAndExpirationDateAfter(Long id, LocalDateTime now);

    /**
     * Gets a verification code by user ID.
     *
     * @param id the user ID
     * @return the verification code associated with the specified user ID
     */
    VerificationCode getVerificationCodeByUserId(Long id);

    /**
     * Deletes a verification code by user ID.
     *
     * @param id the user ID
     */
    void deleteVerificationCodeByUserId(Long id);
}
