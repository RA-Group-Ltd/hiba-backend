package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Address} entities.
 */
@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Finds all addresses associated with the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of addresses associated with the user
     */
    List<Address> findAddressesByUserId(Long userId);

    /**
     * Finds a single address associated with the given user ID.
     *
     * @param userId the ID of the user
     * @return the address associated with the user
     */
    Address findAddressByUserId(Long userId);

}
