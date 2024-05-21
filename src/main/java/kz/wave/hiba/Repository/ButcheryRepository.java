package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Butchery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ButcheryRepository extends JpaRepository<Butchery, Long> {
    List<Butchery> findAll(Specification<Butchery> spec, Sort sortOrder);

    @Query("SELECT count(b) from Butchery b")
    long countButcheries();
}
