package com.bakouan.app.repositories;
import com.bakouan.app.enums.ECarte;
import com.bakouan.app.enums.EStatus;
import com.bakouan.app.enums.EStatut;
import com.bakouan.app.enums.ETypeDemandeur;
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
     * @param eStatut
     * @return
     */
    List<BaDemande> findByStatut(EStatut eStatut);

    /**
     * Requête pour retourner la liste des démandes par statut( ENCOURS, VALIDE, REJETTE, PRODUIT,DELIVRE)
     * @param eStatus
     * @return { Link une liste de demande}
     */
    List<BaDemande> findByStatus(EStatus eStatus);

    /**
     * Requête pour retourner la liste des démandes valider a deux niveaux par statut( ENCOURS, VALIDE, REJETTE, PRODUIT,DELIVRE)
     * @param eStatus
     * @return { Link une liste de demande}
     */
    List<BaDemande> findByStatusAndStatus(EStatus eStatus, EStatus eStatus1);

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
     * @param eCarte
     * @param eStatus
     * @return
     */

    @Query(value = "SELECT d FROM BaDemande d WHERE d.eCarte = :eCarte AND d.status = :eStatus")
    List<BaDemande> findDemandeByCarteAndStatus(@Param("eCarte") ECarte eCarte, @Param("eStatus") EStatus eStatus);



    /**
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR)
     * @param typeDemandeur: type demandeur
     * @return {Link une liste de demande}
     */
    List<BaDemande> findByTypeDemandeur(ETypeDemandeur typeDemandeur);

    @Query("SELECT COUNT(d) FROM BaDemande d WHERE d.eCarte = :eCarte AND YEAR(d.dateDemande) = :annee")
    long countDemandeByTypeAndYear(@Param("eCarte") ECarte eCarte, @Param("annee") int annee);

    boolean existsByNumeroDemande(String numeroDemande);

    /**
     * Statistiques pour les demandes
     */
    /**
     * Le nombre de demande par Status(ENOURS,REJETTE)
     * @return
     */
    @Query("SELECT d.status, COUNT(d) FROM BaDemande d GROUP BY d.status")
    List<Object[]> countDemandesByStatus();

    /**
     * Statistique par type de ecarte
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




}
