package kz.wave.hiba.Repository;

import kz.wave.hiba.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Notification} entities.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
