package com.bakouan.app.controller;

import com.bakouan.app.dto.BaAutorisationSpecialeDto;
import com.bakouan.app.dto.BaDelegationMembreDto;
import com.bakouan.app.service.BaAutorisationService;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.utils.BaConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
public class BaAutorisationController {
    final BaAutorisationService autorisationService;
    final BaFileStorageService baFileStorageService;

    /**
     * Création d'une autorisation spéciale avec upload de la note verbale.
     */
    @PostMapping(BaConstants.URL.AUTORISATION)
    public ResponseEntity<BaAutorisationSpecialeDto> create(
            @Valid final @RequestPart("data") BaAutorisationSpecialeDto autorisationDto,
            final @RequestPart("noteVerbale") MultipartFile noteVerbale) {
        BaAutorisationSpecialeDto created = autorisationService.create(autorisationDto, noteVerbale);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Mise à jour d'une autorisation spéciale.
     */
    @PutMapping(BaConstants.URL.AUTORISATION+"/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> update(
           @Valid @PathVariable final String id,
            @Valid @RequestBody final BaAutorisationSpecialeDto autorisationDto) {
        BaAutorisationSpecialeDto updated = autorisationService.update(id, autorisationDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Suppression (archivage) d'une autorisation spéciale.
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION+"/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        autorisationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Upload d'une note verbale pour une autorisation spéciale.
     */
    @PostMapping(BaConstants.URL.AUTORISATION+"/upload-note-verbale/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> uploadNoteVerbale(
           @Valid  @PathVariable("id") final String autorisationId,
            @RequestParam("file") MultipartFile noteVerbale) {
        BaAutorisationSpecialeDto updated = autorisationService.uploadNoteVerbale(autorisationId, noteVerbale);
        return ResponseEntity.ok(updated);
    }

    /**
     * Suppression d'une note verbale attachée à une autorisation spéciale.
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION+"/{id}/note-verbale/{documentId}")
    public ResponseEntity<BaAutorisationSpecialeDto> removeNoteVerbale(
            @PathVariable("id") String autorisationId,
            @PathVariable("documentId") String documentId) {
        BaAutorisationSpecialeDto updated = autorisationService.removeNoteVerbale(autorisationId, documentId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Récupération d'une autorisation spéciale par son ID.
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(autorisationService.findById(id));
    }

    /**
     * Récupération de toutes les autorisations spéciales actives.
     */
    @GetMapping(BaConstants.URL.AUTORISATION)
    public ResponseEntity<List<BaAutorisationSpecialeDto>> findAll() {
        return ResponseEntity.ok(autorisationService.findAll());
    }

    // Endpoints pour la validation et le rejet

    @PatchMapping(BaConstants.URL.AUTORISATION+"/valider-st/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> validateSt(@PathVariable String id) {
        return ResponseEntity.ok(autorisationService.validateSt(id));
    }

    @PatchMapping(BaConstants.URL.AUTORISATION+"/rejeter-st/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> rejectSt(@PathVariable String id) {
        return ResponseEntity.ok(autorisationService.rejectSt(id));
    }

    @PatchMapping(BaConstants.URL.AUTORISATION+"/valider-dg/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> validateDg(@PathVariable final String id) {
        return ResponseEntity.ok(autorisationService.validateDg(id));
    }

    @PatchMapping(BaConstants.URL.AUTORISATION+"/rejeter-dg/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> rejectDg(@PathVariable final String id) {
        return ResponseEntity.ok(autorisationService.rejectDg(id));
    }

    /**
     * GESTION DES MEMBRE DE LA DEGELATION SPECIALE
     */

    /**
     * Endpoint pour créer un membre de délégation.
     * Le DTO est validé grâce à JSR-303 (annotations dans le DTO) avant d'être traité.
     *
     * Exemple d'URL : POST /api/delegation-speciale/membre
     *
     * @param membreDto Le DTO contenant les informations du membre.
     * @param bindingResult Contient les erreurs de validation, le cas échéant.
     * @return Le membre créé avec le code HTTP 201 (Created).
     */
    @PostMapping(BaConstants.URL.AUTORISATION+"/membre")
    public ResponseEntity<BaDelegationMembreDto> createMember(
            @Valid @RequestBody BaDelegationMembreDto membreDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Ici, vous pouvez retourner un message d'erreur détaillé en fonction des contraintes violées.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur de validation des données du membre.");
        }

        BaDelegationMembreDto createdMember = autorisationService.createMember(membreDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    /**
     * Endpoint pour ajouter un document à un membre de délégation.
     * Le fichier est envoyé en multipart (ex. un document PDF).
     *
     * Exemple d'URL : POST /api/delegation-speciale/membre/{membreId}/document
     *
     * @param membreId Identifiant du membre auquel le document sera associé.
     * @param file Le fichier a ajouter.
     * @return Le membre mis à jour avec le document ajouté.
     */
    @PostMapping(BaConstants.URL.AUTORISATION+"/document/{membreId}")
    public ResponseEntity<BaDelegationMembreDto> addDocumentToMember(
            @PathVariable("membreId") String membreId,
            @Valid @RequestPart("file") MultipartFile file) {

        BaDelegationMembreDto updatedMember = autorisationService.addDocumentToMember(membreId, file);
        return ResponseEntity.ok(updatedMember);
    }

    /**
     * Endpoint pour supprimer un document d'un membre de délégation.
     *
     * Exemple d'URL : DELETE /api/delegation-speciale/membre/{membreId}/document/{documentId}
     *
     * @param membreId Identifiant du membre concerné.
     * @param documentId Identifiant du document à supprimer.
     * @return Le membre mis à jour après la suppression du document.
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION+"/document/{membreId}/{documentId}")
    public ResponseEntity<BaDelegationMembreDto> removeDocumentFromMember(
            @PathVariable("membreId") String membreId,
            @PathVariable("documentId") String documentId) {

        BaDelegationMembreDto updatedMember = autorisationService.removeDocumentFromMember(membreId, documentId);
        return ResponseEntity.ok(updatedMember);
    }

    /**
     * (Optionnel) Endpoint pour récupérer les détails d'un membre de délégation.
     *
     * Exemple d'URL : GET /api/delegation-speciale/membre/{membreId}
     *
     * @param membreId Identifiant du membre à récupérer.
     * @return Le membre correspondant.
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/membre/{membreId}")
    public ResponseEntity<BaDelegationMembreDto> getMember(@PathVariable("membreId") String membreId) {
        BaDelegationMembreDto memberDto = autorisationService.getMemberById(membreId);
        return ResponseEntity.ok(memberDto);
    }

    /**
     * (Optionnel) Endpoint pour récupérer tous les membres de délégation.
     *
     * Exemple d'URL : GET /api/delegation-speciale/membre
     *
     * @return La liste de tous les membres de délégation.
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/membre")
    public ResponseEntity<List<BaDelegationMembreDto>> getAllMembers() {
        List<BaDelegationMembreDto> members = autorisationService.getAllMembers();
        return ResponseEntity.ok(members);
    }

}
