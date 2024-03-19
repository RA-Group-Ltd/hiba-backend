package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    List<VerificationCode> findByUserIdAndExpirationDateAfter(Long id, LocalDateTime now);
    VerificationCode getVerificationCodeByUserId(Long id);
    void deleteVerificationCodeByUserId(Long id);
}
