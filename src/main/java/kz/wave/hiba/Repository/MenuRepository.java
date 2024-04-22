package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> getMenusByButcheryCategoryId(Long id);
    Menu getMenuById(Long id);
}
