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
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR)
     *
     * @param eCarte
     * @return {Link une liste de demande}
     */
    @Query("SELECT d FROM BaDemande d WHERE d.eCarte = :eCarte")
    List<BaDemande> findByECarte(@Param("eCarte") ECarte eCarte);



    /**
     * Requête pour retourner la liste des demandes par type de carte (DIPLOMATIQUE, SALON D'HONNEUR)
     * @param typeDemandeur: type demandeur
     * @return {Link une liste de demande}
     */
    List<BaDemande> findByTypeDemandeur(ETypeDemandeur typeDemandeur);
}
