package com.bakouan.app.service;

import com.bakouan.app.dto.BaLogDto;
import com.bakouan.app.enums.EAction;
import com.bakouan.app.model.BaAutorisationSpeciale;
import com.bakouan.app.repositories.BaAutorisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
@RequiredArgsConstructor
@Slf4j
public class BaAsynchroService {
    private final BaAutorisationRepository autorisationRepository;
    private final BaLogService logService;
    private final BaMailService mailService;

    /**
     * Ce service traite l'envoi de mail après validation, il a pour rôle de rendre fluide l'execution et
     * l'envoi de mail sans pendre assez de temps et éviter surtout l'erreur 504
     * @param id: id de l'autorisation
     */
    @Async("taskExecutor")
    public void apresValideAutorisation(String id) {
        // Log
        logService.log(new BaLogDto(EAction.U, "Soumission de la demande ID : " + id + " par l'utilisateur"));

        // Chargement uniquement pour récupérer user & numDemande
        BaAutorisationSpeciale a = autorisationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation introuvable : " + id));

        String userFullName = a.getUser().getNom() + " " + a.getUser().getPrenom();
        String email        = a.getUser().getEmail();
        String numDemande   = a.getNumDemande();

        // Envoi d’email
        mailService.sendMessage(
                email,
                "Bonjour " + userFullName,
                "Votre demande d'autorisation spéciale " + numDemande + " a bien été soumise et est en attente de traitement.",
                "Confirmation de soumission"
        );
    }

    /**
     * Après validation synchronisée, gérer le log et l'envoi de mail en tâche de fond.
     */
    @Async("taskExecutor")
    public void afterValidate(String id) {
        logService.log(new BaLogDto(EAction.U, "Validation autorisation ID : " + id));

        BaAutorisationSpeciale autorisation = autorisationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Autorisation introuvable : " + id));

        String fullName = autorisation.getUser().getNom() + " " + autorisation.getUser().getPrenom();
        String numeroDemande = autorisation.getNumDemande();

        // 4. Envoi du mail
        mailService.sendMessage(
                autorisation.getUser().getEmail(),
                "À " + fullName,
                "Votre demande d'autorisation spéciale de passage " + numeroDemande + " a été acceptée.",
                "Autorisation spéciale"
        );

        log.info("Tâche asynchrone afterValidate terminée pour ID {}", id);
    }

}
