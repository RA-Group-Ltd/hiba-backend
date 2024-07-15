package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Country} entities.
 */
@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {

    /**
     * Finds a country by its name.
     *
     * @param name the name of the country
     * @return the country with the specified name
     */
    Country findByName(String name);
}
