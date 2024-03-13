package kz.wave.hiba.Controller;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.MenuRepository;
import kz.wave.hiba.Specification.ButcherySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/butcher")
public class ButcherController {

    @Autowired
    private ButcherRepository butcherRepository;

    @Autowired
    private ButcheryRepository butcheryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping(value = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchButcheries(@RequestParam(value = "q", required = false) String q,
                                              @RequestParam(value = "categories", required = false) Integer[] categories,
                                              @RequestParam(value = "latitude", required = false) Float latitude,
                                              @RequestParam(value = "longitude", required = false) Float longitude,
                                              @RequestParam(value = "sort", defaultValue = "name") String sort) {

        Specification<Butchery> spec = Specification.where(ButcherySpecification.hasNameLike(q))
                .and(ButcherySpecification.isCategoryIn(categories));

        // Пример добавления дополнительных условий
        // Здесь можете добавлять условия по latitude и longitude, если у вас есть соответствующая логика фильтрации

        Sort sortOrder = sort.equalsIgnoreCase("name") ? Sort.by("name").descending() : Sort.unsorted();

        List<Butchery> result = butcheryRepository.findAll(spec, sortOrder);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getByButcherId(@PathVariable Long id) {
        return butcherRepository.findById(id)
                .map(butchery -> ResponseEntity.ok().body(butchery))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
