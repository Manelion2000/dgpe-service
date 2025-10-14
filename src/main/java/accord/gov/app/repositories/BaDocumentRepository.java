package accord.gov.app.repositories;

import accord.gov.app.enums.*;
import accord.gov.app.model.BaDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/07/2025 à 19:19
 */
@Repository
public interface BaDocumentRepository extends JpaRepository<BaDocument, String>, JpaSpecificationExecutor<BaDocument> {

    List<BaDocument> findByStatutAndConfidentialite(EStatut eStatut, EConfidentiel eConfidentiel);
    List<BaDocument> findByDateSignatureBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Statistique
     */
    @Query("SELECT d.natureDocument, COUNT(d) " +
            "FROM BaDocument d " +
            "GROUP BY d.natureDocument")
    List<Object[]> countByNatureDocument();

    @Query("SELECT dom.libelle, COUNT(d) " +
            "FROM BaDocument d JOIN d.domaines dom " +
            "GROUP BY dom.libelle")
    List<Object[]> countByDomaine();

    @Query("SELECT p.libelle, COUNT(d) " +
            "FROM BaDocument d JOIN d.parties p " +
            "GROUP BY p.libelle")
    List<Object[]> countByPartiePrenante();

    // Vérifier si un typeDocument existe
    boolean existsByTypeDocumentId(String typeId);

    // Récupérer seulement les IDs de langues qui existent dans les documents
    @Query("SELECT DISTINCT l.id FROM BaDocument d JOIN d.langues l WHERE l.id IN :ids")
    List<String> findExistingLangueIds(@Param("ids") List<String> ids);

    // Récupérer seulement les IDs de domaines qui existent dans les documents
    @Query("SELECT DISTINCT dom.id FROM BaDocument d JOIN d.domaines dom WHERE dom.id IN :ids")
    List<String> findExistingDomaineIds(@Param("ids") List<String> ids);

    // Récupérer seulement les IDs de parties prenantes qui existent dans les documents
    @Query("SELECT DISTINCT p.id FROM BaDocument d JOIN d.parties p WHERE p.id IN :ids")
    List<String> findExistingPartieIds(@Param("ids") List<String> ids);

    @Query(value = """
            SELECT DISTINCT d.*
                FROM ba_document d
                LEFT JOIN ba_document_langue dl ON d.id = dl.document_id
                LEFT JOIN ba_document_domaine dd ON d.id = dd.document_id
                LEFT JOIN ba_document_partie dp ON d.id = dp.document_id
                WHERE (:typeId IS NULL OR d.type_document_id = CAST(:typeId AS text))
                  AND (:nature IS NULL OR d.nature_document = :nature)
                  AND (array_length(CAST(:langueIds AS text[]), 1) IS NULL OR dl.langue_id = ANY(CAST(:langueIds AS text[])))
                  AND (array_length(CAST(:domaineIds AS text[]), 1) IS NULL OR dd.domaine_id = ANY(CAST(:domaineIds AS text[])))
                  AND (array_length(CAST(:partieIds AS text[]), 1) IS NULL OR dp.partie_id = ANY(CAST(:partieIds AS text[])))
                  AND (array_length(CAST(:keywords AS text[]), 1) IS NULL OR (
                        unaccent(LOWER(d.mot_cle)) LIKE ANY(CAST(:keywords AS text[]))
                        OR unaccent(LOWER(d.resume)) LIKE ANY(CAST(:keywords AS text[]))
                  ))
    """, nativeQuery = true)
    List<BaDocument> searchDocumentStrict(
            @Param("typeId") String typeId,
            @Param("nature") String nature,
            @Param("langueIds") String[] langueIds,
            @Param("domaineIds") String[] domaineIds,
            @Param("partieIds") String[] partieIds,
            @Param("keywords") String[] keywords
    );


    @Query(value = """
            SELECT DISTINCT d.*
                FROM ba_document d
                LEFT JOIN ba_document_langue dl ON d.id = dl.document_id
                LEFT JOIN ba_document_domaine dd ON d.id = dd.document_id
                LEFT JOIN ba_document_partie dp ON d.id = dp.document_id
                WHERE (:typeId IS NULL OR d.type_document_id = CAST(:typeId AS text))
                  OR (:nature IS NULL OR d.nature_document = :nature)
                  OR (array_length(CAST(:langueIds AS text[]), 1) IS NULL OR dl.langue_id = ANY(CAST(:langueIds AS text[])))
                  OR (array_length(CAST(:domaineIds AS text[]), 1) IS NULL OR dd.domaine_id = ANY(CAST(:domaineIds AS text[])))
                  OR (array_length(CAST(:partieIds AS text[]), 1) IS NULL OR dp.partie_id = ANY(CAST(:partieIds AS text[])))
                  OR (array_length(CAST(:keywords AS text[]), 1) IS NULL OR (
                        unaccent(LOWER(d.mot_cle)) LIKE ANY(CAST(:keywords AS text[]))
                        OR unaccent(LOWER(d.resume)) LIKE ANY(CAST(:keywords AS text[]))
                  ))
    """, nativeQuery = true)
    List<BaDocument> searchDocumentLarge(
            @Param("typeId") String typeId,
            @Param("nature") String nature,
            @Param("langueIds") String[] langueIds,
            @Param("domaineIds") String[] domaineIds,
            @Param("partieIds") String[] partieIds,
            @Param("keywords") String[] keywords
    );

    /**
     *
     * @param typeId: type d'accord ou de traité (son Id): obligatoire
     * @param nature: la nature du document : obligatoire
     * @param langueIds: id de langues( ids des langues)
     * @param domaineIds: id des domaines
     * @param partieIds: id de partie prénante: obligatoire et vraie
     * @param keywords: mots clés
     * @return : liste des documents
     */
    @Query(value = """
    SELECT DISTINCT d.*
    FROM ba_document d
    LEFT JOIN ba_document_langue dl ON d.id = dl.document_id
    LEFT JOIN ba_document_domaine dd ON d.id = dd.document_id
    LEFT JOIN ba_document_partie dp ON d.id = dp.document_id
    WHERE 
          -- Partie stricte (AND)
          (:typeId IS NULL OR d.type_document_id = CAST(:typeId AS text))
      AND (:nature IS NULL OR d.nature_document = :nature)
      AND dp.partie_id = ANY(CAST(:partieIds AS text[]))

          -- Partie large (OR)
      AND (
            (array_length(CAST(:langueIds AS text[]), 1) IS NULL 
             AND array_length(CAST(:domaineIds AS text[]), 1) IS NULL 
             AND array_length(CAST(:keywords AS text[]), 1) IS NULL
            )
         OR (array_length(CAST(:langueIds AS text[]), 1) IS NOT NULL 
                AND dl.langue_id = ANY(CAST(:langueIds AS text[])))
         OR (array_length(CAST(:domaineIds AS text[]), 1) IS NOT NULL 
                AND dd.domaine_id = ANY(CAST(:domaineIds AS text[])))
         OR (array_length(CAST(:partieIds AS text[]), 1) IS NOT NULL 
                AND dp.partie_id = ANY(CAST(:partieIds AS text[])))
         OR (array_length(CAST(:keywords AS text[]), 1) IS NOT NULL AND (
                unaccent(LOWER(d.mot_cle)) LIKE ANY(CAST(:keywords AS text[]))
             OR unaccent(LOWER(d.resume)) LIKE ANY(CAST(:keywords AS text[]))
            ))
      )
    """, nativeQuery = true)
    List<BaDocument> searchDocumentsMixte(
            @Param("typeId") String typeId,
            @Param("nature") String nature,
            @Param("partieIds") String[] partieIds,
            @Param("langueIds") String[] langueIds,
            @Param("domaineIds") String[] domaineIds,
            @Param("keywords") String[] keywords
    );





}
