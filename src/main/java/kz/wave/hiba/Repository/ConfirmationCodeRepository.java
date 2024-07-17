package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    ConfirmationCode getConfirmationCodeByUserId(Long id);

}
