package accord.gov.app.repositories.specification;

import accord.gov.app.model.BaDocument;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 15/09/2025 à 01:53
 */
public class BaDocumentSpecification {

    public static Specification<BaDocument> filter(
            String typeAccordId,
            List<String> langueIds,
            List<String> domaineIds,
            List<String> partieIds,
            List<String> motsCles,
            String nature
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (typeAccordId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("typeDocument").get("id"), typeAccordId));
            }

            if (langueIds != null && !langueIds.isEmpty()) {
                predicate = cb.and(predicate,
                        root.join("langues").get("id").in(langueIds));
            }

            if (domaineIds != null && !domaineIds.isEmpty()) {
                predicate = cb.and(predicate,
                        root.join("domaines").get("id").in(domaineIds));
            }

            if (partieIds != null && !partieIds.isEmpty()) {
                predicate = cb.and(predicate,
                        root.join("parties").get("id").in(partieIds));
            }

            if (motsCles != null && !motsCles.isEmpty()) {
                Predicate motsPredicate = cb.disjunction();
                for (String mot : motsCles) {
                    motsPredicate = cb.or(motsPredicate,
                            cb.like(cb.lower(root.get("libelle")), "%" + mot.toLowerCase() + "%"));
                }
                predicate = cb.and(predicate, motsPredicate);
            }

            if (nature != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("natureDocument"), nature));
            }

            return predicate;
        };
    }
}
