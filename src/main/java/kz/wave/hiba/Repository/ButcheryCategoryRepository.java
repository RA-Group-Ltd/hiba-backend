package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.ButcheryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link ButcheryCategory} entities.
 */
@Repository
@Transactional
public interface ButcheryCategoryRepository extends JpaRepository<ButcheryCategory, Long> {

    /**
     * Finds all butchery categories associated with a specific butchery ID.
     *
     * @param id the ID of the butchery
     * @return a list of butchery categories associated with the butchery ID
     */
    List<ButcheryCategory> getButcheryCategoriesByButcheryId(Long id);
}
