package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Menu} entities.
 */
@Repository
@Transactional
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Finds all menus by the butchery category ID.
     *
     * @param id the butchery category ID
     * @return a list of menus associated with the butchery category ID
     */
    List<Menu> getMenusByButcheryCategoryId(Long id);

    /**
     * Finds a menu by its ID.
     *
     * @param id the ID of the menu
     * @return the menu with the specified ID
     */
    Menu getMenuById(Long id);
}
