package com.bakouan.app.repositories;
import com.bakouan.app.dto.BaStatistiqueCarteDto;
import com.bakouan.app.dto.BaStatistiqueTotalDto;
import com.bakouan.app.enums.*;
import com.bakouan.app.model.BaDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BaDemandeRepository extends JpaRepository<BaDemande,String> {
    boolean existsById(String id);

    /**
     * Requête pour retourner la liste des démandes par statut( Actif ou Desactivé)
     *
     * @param eStatut
     * @return
     */
    List<BaDemande> findByStatut(EStatut eStatut);

    /**
     * Requête pour retourner la liste des démandes par statut( ENCOURS, VALIDE, REJETTE, PRODUIT,DELIVRE)
     *
     * @param eStatus
     * @return { Link une liste de demande}
     */
    List<BaDemande> findByStatus(EStatus eStatus);

    /**
     * Requête pour retourner la liste des démandes par statut( ENCOURS, VALIDE, REJETTE, PRODUIT,DELIVRE) par DG
     *
     * @param eStatus
     * @return { Link une liste de demande}
     */
    List<BaDemande> findByStatusDg(EstatusDg eStatus);

    /**
     * Requête pour retourner la liste des démandes valider a deux niveaux par statut( ENCOURS, VALIDE, REJETTE, PRODUIT,DELIVRE)
     *
     * @param eStatus
     * @return { Link une liste de demande}
     */
    List<BaDemande> findByStatusAndStatusDg(EStatus eStatus, EstatusDg eStatusDg);

    /**
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR)
     *
     * @param eCarte
     * @return {Link une liste de demande}
     */
    @Query("SELECT d FROM BaDemande d WHERE d.eCarte = :eCarte")
    List<BaDemande> findByECarte(@Param("eCarte") ECarte eCarte);

    /**
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR) et par stutus(ENCOUR)
     *
     * @param eCarte
     * @param eStatus
     * @return
     */

    @Query(value = "SELECT d FROM BaDemande d WHERE d.eCarte = :eCarte AND d.status = :eStatus")
    List<BaDemande> findDemandeByCarteAndStatus(@Param("eCarte") ECarte eCarte, @Param("eStatus") EStatus eStatus);


    /**
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR)
     *
     * @param typeDemandeur: type demandeur
     * @return {Link une liste de demande}
     */
    List<BaDemande> findByDemandeur(ETypeDemandeur typeDemandeur);

    @Query("SELECT COUNT(d) FROM BaDemande d WHERE d.eCarte = :eCarte AND YEAR(d.dateDemande) = :annee")
    long countDemandeByTypeAndYear(@Param("eCarte") ECarte eCarte, @Param("annee") int annee);

    boolean existsByNumeroDemande(String numeroDemande);

    /**
     * La Liste des demandes par utilisateur
     * @param userId
     * @return
     */
    @Query("SELECT d FROM BaDemande d WHERE d.user.id = :userId")
    List<BaDemande> findDemandesByUserId(@Param("userId") String userId);



    /**
     * Statistiques pour les demandes
     */
    /**
     * Le nombre de demande par Status(ENOURS,REJETTE)
     *
     * <p>Le nombre de demande par Status(ENOURS,REJETTE)
     * @return
     */
    @Query("SELECT d.status, COUNT(d) FROM BaDemande d GROUP BY d.status")
    List<Object[]> countDemandesByStatus();

    /**
     * Statistique par type de ecarte
     *
     * @return
     */
    @Query("SELECT d.eCarte, COUNT(d) FROM BaDemande d GROUP BY d.eCarte")
    List<Object[]> countDemandesByCarte();

    /**
     * Statistique de demande par mois
     */
    @Query("SELECT MONTH(d.dateDemande), COUNT(d) FROM BaDemande d GROUP BY MONTH(d.dateDemande)")
    List<Object[]> countDemandesByMonth();

    /**
     * Nombre demande par utisateur
     */

    @Query("SELECT d.user.id, COUNT(d) FROM BaDemande d GROUP BY d.user.id")
    List<Object[]> countDemandesByUser();

    /**
     * Liste des démandes rejettées ou rejetter
     *
     * @param validerStatus
     * @param rejeterStatus
     * @param carteAcces
     * @return
     */

    @Query("SELECT d FROM BaDemande d WHERE d.status = :validerStatus OR (d.status = :rejeterStatus AND d.eCarte = :carteAcces)")
    List<BaDemande> findValidOrRejectedAccessCards(@Param("validerStatus") EStatus validerStatus,
                                                   @Param("rejeterStatus") EStatus rejeterStatus,
                                                   @Param("carteAcces") ECarte carteAcces);

    /**
     * Liste de demande de cartes rejettées (REJETER, REJETER_DG)
     *
     * @param eCarte
     * @param statuses
     * @return
     */
    @Query("SELECT d FROM BaDemande d WHERE d.eCarte = :eCarte AND d.status IN (:statuses)")
    List<BaDemande> findDemandeRejectedByDGAndCarte(@Param("eCarte") ECarte eCarte, @Param("statuses") List<EStatus> statuses);

    @Query("SELECT new com.bakouan.app.dto.BaStatistiqueTotalDto(" +
            "COUNT(d), " +
            "SUM(CASE WHEN d.status = 'ENCOURS' OR d.status = 'VALIDER' OR (d.eCarte = 'CARTE_ACCES' AND d.status = 'REJETER') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'VALIDER_DG' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN (d.eCarte = 'CARTE_DIPLOMATIQUE' AND (d.status = 'REJETER' OR d.status = 'REJETER_DG')) OR " +
            "             (d.eCarte = 'CARTE_ACCES' AND d.status = 'REJETER_DG') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'PRODUIT' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'DELIVRE' THEN 1 ELSE 0 END)) " +
            "FROM BaDemande d")
    BaStatistiqueTotalDto getGlobalStatistics();

    @Query("SELECT new com.bakouan.app.dto.BaStatistiqueTotalDto(" +
            "COUNT(d), " +
            "SUM(CASE WHEN d.status = 'ENCOURS' OR d.status = 'VALIDER' OR " +
            "             (d.eCarte = 'CARTE_ACCES' AND d.status = 'REJETER') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'VALIDER_DG' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN (d.eCarte = 'CARTE_DIPLOMATIQUE' AND (d.status = 'REJETER' OR d.status = 'REJETER_DG')) OR " +
            "             (d.eCarte = 'CARTE_ACCES' AND d.status = 'REJETER_DG') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'PRODUIT' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.status = 'DELIVRE' THEN 1 ELSE 0 END)) " +
            "FROM BaDemande d WHERE d.eCarte = :eCarte")
    BaStatistiqueTotalDto getStatisticsByCarte(@Param("eCarte") ECarte eCarte);

    /**
     * la requête permettant d'avoir le nombre de demandes par mois et pour une année courante
     * @param annee: l'année courante
     * @param eCarte: le type de carte
     * @return
     */
    @Query("SELECT MONTH(d.dateDemande), COUNT(d) " +
            "FROM BaDemande d " +
            "WHERE YEAR(d.dateDemande) = :annee AND d.eCarte = :eCarte " +
            "GROUP BY MONTH(d.dateDemande)")
    List<Object[]> countDemandesByMonthAndType(@Param("annee") int annee, @Param("eCarte") ECarte eCarte);

    @Query("SELECT new com.bakouan.app.dto.BaStatistiqueCarteDto( " +
            "SUM(CASE WHEN d.eCarte = 'CARTE_DIPLOMATIQUE' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN d.eCarte = 'CARTE_ACCES' THEN 1 ELSE 0 END)) " +
            "FROM BaDemande d " +
            "WHERE EXTRACT(YEAR FROM d.dateDemande) = :annee")
    BaStatistiqueCarteDto countCarteByTypeAndYear(@Param("annee") int annee);







}
