package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link City} entities.
 */
@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * Finds all city IDs by country and a list of city names.
     *
     * @param country the country
     * @param cityNames the array of city names
     * @return a list of city IDs that match the specified country and city names
     */
    @Query("SELECT c.id FROM City c WHERE c.country = :country AND c.name IN :names")
    List<Long> findAllIdsByCountryAndNameInList(@Param("country") Country country, @Param("names") String[] cityNames);
}
