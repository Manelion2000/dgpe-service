package accord.gov.app.repositories.specification;

import accord.gov.app.enums.ENatureDocument;
import accord.gov.app.model.BaDocument;
import accord.gov.app.model.BaDomaine;
import accord.gov.app.model.BaLangue;
import accord.gov.app.model.BaPartie;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 15/09/2025 à 01:53
 */

@Component
public class BaDocumentSpecification {

    // ✅ Filtre par Parties
    public static Specification<BaDocument> byParties(List<String> partieIds) {
        return (root, query, cb) -> {
            if (partieIds == null) {
                return cb.conjunction(); // pas fourni
            }
            if (partieIds.isEmpty()) {
                return cb.disjunction(); // fourni mais vide → aucun résultat
            }
            query.distinct(true);
            Join<BaDocument, BaPartie> join = root.join("parties", JoinType.LEFT);
            return join.get("id").in(partieIds);
        };
    }

    // ✅ Filtre par Domaines
    public static Specification<BaDocument> byDomaines(List<String> domaineIds) {
        return (root, query, cb) -> {
            if (domaineIds == null) {
                return cb.conjunction();
            }
            if (domaineIds.isEmpty()) {
                return cb.disjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<BaDocument, BaDomaine> join = root.join("domaines", JoinType.LEFT);
            return join.get("id").in(domaineIds);
        };
    }

    // ✅ Filtre par Langues
    public static Specification<BaDocument> byLangues(List<String> langueIds) {
        return (root, query, cb) -> {
            if (langueIds == null) {
                return cb.conjunction();
            }
            if (langueIds.isEmpty()) {
                return cb.disjunction();
            }
            query.distinct(true);
            Join<BaDocument, BaLangue> join = root.join("langues", JoinType.LEFT);
            return join.get("id").in(langueIds);
        };
    }

    // ✅ Filtre par Type Accord
    public static Specification<BaDocument> byTypeAccord(String typeAccordId) {
        return (root, query, cb) -> {
            if (typeAccordId == null) {
                return cb.conjunction();
            }
            if (typeAccordId.isEmpty()) {
                return cb.disjunction();
            }
            return cb.equal(root.get("typeAccord").get("id"), typeAccordId);
        };
    }

    // ✅ Filtre par Nature
    public static Specification<BaDocument> byNature(ENatureDocument nature) {
        return (root, query, cb) -> {
            if (nature == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("nature"), nature);
        };
    }

    // ✅ Filtre par mots-clés
    public static Specification<BaDocument> byMotsCles(List<String> motsCles) {
        return (root, query, cb) -> {
            if (motsCles == null) {
                return cb.conjunction();
            }
            if (motsCles.isEmpty()) {
                return cb.disjunction();
            }
            return cb.or(
                    motsCles.stream()
                            .flatMap(mot -> {
                                String pattern = "%" + mot.toLowerCase() + "%";
                                return Stream.of(
                                        cb.like(cb.lower(root.get("libelle")), pattern),
                                        cb.like(cb.lower(root.get("motCle")), pattern),
                                        cb.like(cb.lower(root.get("resume")), pattern)
                                );
                            })
                            .toArray(jakarta.persistence.criteria.Predicate[]::new)
            );
        };
    }
}
