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

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getByButcherId(@PathVariable Long id) {
        return butcherRepository.findById(id)
                .map(butchery -> ResponseEntity.ok().body(butchery))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
