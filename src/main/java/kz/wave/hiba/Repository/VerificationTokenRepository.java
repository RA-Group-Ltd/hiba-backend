package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface VerificationTokenRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByToken(String token);

}
