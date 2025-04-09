package com.bakouan.app.repositories;

import com.bakouan.app.model.BaDelegationMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaDelegationMembreRepository extends JpaRepository<BaDelegationMembre, String> {
    /**
     * Méthode pour récupérer la liste des membres associés à une autorisation spéciale donnée.
     * La recherche se fait par l'ID de l'autorisation spéciale (champ 'autorisationSpeciale.id').
     *
     * @param autorisationSpecialeId l'ID de l'autorisation spéciale.
     * @return Liste des membres liés à cette autorisation spéciale.
     */
    List<BaDelegationMembre> findByAutorisationSpecialeId(String autorisationSpecialeId);
}
