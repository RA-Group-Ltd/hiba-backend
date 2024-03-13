package kz.wave.hiba.Specification;

import kz.wave.hiba.Entities.Butchery;
import org.springframework.data.jpa.domain.Specification;

public class ButcherySpecification {

    public static Specification<Butchery> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null) return cb.isTrue(cb.literal(true));
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Butchery> isCategoryIn(Integer[] categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.length == 0) return cb.isTrue(cb.literal(true));
            return root.join("categories").get("id").in((Object[]) categories);
        };
    }

}
