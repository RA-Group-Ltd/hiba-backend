package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByClientIdAndSupportId(Long clientId, Long supportId);
//    Optional<Chat> findByClient
    List<Chat> findByClientId(Long clientId);
    List<Chat> findBySupportId(Long supportId);
//    List<Chat> findChatsBySupportIdIsNull();

    List<Chat> findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse(boolean isButchery);
    List<Chat> findChatsByArchiveIsTrueAndIsButchery(boolean isButchery);
    List<Chat> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery(boolean isButchery);

}
