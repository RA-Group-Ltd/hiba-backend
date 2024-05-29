package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT c.id FROM City c WHERE c.country = :country AND c.name IN :names")
    List<Long> findAllIdsByCountryAndNameInList(@Param("country") Country country, @Param("names") String[] cityNames);
}
