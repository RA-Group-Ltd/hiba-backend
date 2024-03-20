package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.ButcheryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ButcheryCategoryRepository extends JpaRepository<ButcheryCategory, Long> {

    List<ButcheryCategory> getButcheryCategoriesByButcheryId(Long id);
}
