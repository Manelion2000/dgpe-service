package com.bakouan.app.controller;

import com.bakouan.app.dto.BaAutorisationSpecialeDto;
import com.bakouan.app.dto.BaDelegationMembreDto;
import com.bakouan.app.dto.BaDocumentAutorisationSpecialDto;
import com.bakouan.app.dto.BaDocumentPersonnelAutorisationSpecialDto;
import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.ETypeAutorisation;
import com.bakouan.app.model.BaDocumentAutorisationSpecial;
import com.bakouan.app.repositories.BaDocumentAutorisationRepository;
import com.bakouan.app.service.BaAutorisationService;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.utils.BaConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
    final BaDocumentAutorisationRepository documentAutorisationRepository;

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
     * Récupérer toutes les autorisations spéciales pour un utilisateur donné.
     *
     * @param userId identifiant de l'utilisateur
     * @return liste des autorisations spéciales
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/user/{userId}")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByUser(@PathVariable String userId) {
        List<BaAutorisationSpecialeDto> autorisations = autorisationService.findAllByUser(userId);
        return ResponseEntity.ok(autorisations);
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
     * Validation d'une autorisation spéciale
     * @param id: identifiant de la demande
     * @return : une chaine de caractère
     */
    @PatchMapping(BaConstants.URL.AUTORISATION + "/soumettre/{id}")
    public ResponseEntity<String> validerSoumissionDemande(@PathVariable String id) {
        autorisationService.SoumettreDemande(id);
        return ResponseEntity.ok("La demande a été soumise avec succès et est en attente de traitement.");
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
    @PostMapping(BaConstants.URL.AUTORISATION+"/upload_note_verbale/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> uploadNoteVerbale(
           @Valid  @PathVariable("id") final String autorisationId,
            @RequestParam("file") MultipartFile noteVerbale) {
        BaAutorisationSpecialeDto updated = autorisationService.uploadNoteVerbale(autorisationId, noteVerbale);
        return ResponseEntity.ok(updated);
    }

    /**
     * Suppression d'une note verbale attachée à une autorisation spéciale.
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION+"/note-verbale/{id}/{documentId}")
    public ResponseEntity<BaAutorisationSpecialeDto> removeNoteVerbale(
            @PathVariable("id") String autorisationId,
            @PathVariable("documentId") String documentId) {
        BaAutorisationSpecialeDto updated = autorisationService.removeNoteVerbale(autorisationId, documentId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Service pour supprimer un membre de la liste d'une spéciale
     * @param id : Id du membre de la delagation
     * @return : null
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION + "/membre/{id}")
    public ResponseEntity<Void> supprimerMembre(@PathVariable String id) {
        autorisationService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Récupération d'une autorisation spéciale par son ID.
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/{id}")
    public ResponseEntity<BaAutorisationSpecialeDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(autorisationService.findById(id));
    }
    /**
     * Récupération des autorisations spéciales selon l'état fourni.
     * @return liste des autorisations filtrées
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/attente")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByEtatAttente() {
        List<BaAutorisationSpecialeDto> result = autorisationService.findValiderParEtat(EEtatAutorisation.EN_ATTENTE);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupération des autorisations spéciales selon l'état fourni.
     * @return liste des autorisations filtrées
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/valide")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByEtatValide() {
        List<BaAutorisationSpecialeDto> result = autorisationService.findValiderParEtat(EEtatAutorisation.VALIDE);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupération des autorisations spéciales selon l'état fourni.
     * @return liste des autorisations filtrées
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/rejette")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByRejette() {
        List<BaAutorisationSpecialeDto> result = autorisationService.findValiderParEtat(EEtatAutorisation.REJETE);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste des autorisations uniquement pour depart
     * @return: une liste
     */
    @GetMapping(BaConstants.URL.AUTORISATION + "/depart")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByType() {
        return ResponseEntity.ok(autorisationService.findByType(ETypeAutorisation.DEPART));
    }

  /**
     * Liste des autorisations uniquement pour arrrivee
     * @return: une liste
     */
    @GetMapping(BaConstants.URL.AUTORISATION + "/arrivee")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByArrivee() {
        return ResponseEntity.ok(autorisationService.findByType(ETypeAutorisation.ARRIVEE));
    }

    /**
     * Liste des autorisations uniquement pour arrrivee_depart
     * @return: une liste
     */

    @GetMapping(BaConstants.URL.AUTORISATION + "/arrivee_depart")
    public ResponseEntity<List<BaAutorisationSpecialeDto>> getByArriveeDepart() {
        return ResponseEntity.ok(autorisationService.findByType(ETypeAutorisation.ARRIVEE_DEPART));
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
    public ResponseEntity<BaAutorisationSpecialeDto> rejectAutorisationByST(
            @PathVariable String id,
            @RequestBody BaAutorisationSpecialeDto dto) {
        return ResponseEntity.ok(autorisationService.rejectSt(id, dto));
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
     * @param membreDto Le DTO contenant les informations du membre.
     * @return Le membre créé avec le code HTTP 201 (Created).
     */
    @PostMapping(BaConstants.URL.AUTORISATION+"/membre")
    public ResponseEntity<BaDelegationMembreDto> createMembre(
            @RequestPart("membre") @Valid BaDelegationMembreDto membreDto,
            @RequestPart(value = "documents", required = true) List<BaDocumentPersonnelAutorisationSpecialDto> docDtos,
            @RequestPart(value = "files", required = true) List<MultipartFile> files
    ) {
        BaDelegationMembreDto saved = autorisationService.createMember(membreDto, docDtos, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Endpoint pour ajouter un document à un membre de délégation.
     * Le fichier est envoyé en multipart (ex. un document PDF).
     * Exemple d'URL : POST /api/delegation-speciale/membre/{membreId}/document
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
     * @return La liste de tous les membres de délégation.
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/membre")
    public ResponseEntity<List<BaDelegationMembreDto>> getAllMembers() {
        List<BaDelegationMembreDto> members = autorisationService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    /**
     * Endpoint REST pour récupérer la liste des membres associés à une autorisation spéciale.
     * @param autorisationSpecialeId l'ID de l'autorisation spéciale
     * @return la liste des membres sous forme de DTO avec le code HTTP 200 (OK)
     */
    @GetMapping(BaConstants.URL.AUTORISATION+"/delegation/autorisationSpecialeId")
    public ResponseEntity<List<BaDelegationMembreDto>> getMembersByAutorisationSpeciale(
            @PathVariable String autorisationSpecialeId) {
        // Appel au service pour récupérer les membres associés à l'autorisation spéciale
        List<BaDelegationMembreDto> membres = autorisationService.getMembersByAutorisationSpeciale(autorisationSpecialeId);
        return ResponseEntity.ok(membres);
    }
/**
 * Service de Lecture des different document de l'autorisation spéciale et du personnel
 */
    /**
     * Fonction permettant de retourner un tableau de Byte d'un document pour une autorisation spéciale
     * @param idDoc : id de document
     * @return un tableau de Byte
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/lecture/autorisation/{idDoc}")
    public ResponseEntity<byte[]> lireDocument(@PathVariable final String idDoc) {
        return new ResponseEntity<>(baFileStorageService.getDocumentAutorisationSpeciale(idDoc), HttpStatus.OK);
    }

    /**
     * Endpoint pour visualiser ou télécharger un document d'autorisation spéciale.
     * - Si `download=true`, le document est téléchargé.
     * - Sinon, il est affiché dans le navigateur (visualisation PDF).
     * Exemple d'URL :
     * - Visualiser : GET /api/document/lecture/autorisation/{idDoc}
     * - Télécharger : GET /api/document/lecture/autorisation/{idDoc}?download=true
     *
     * @param idDoc L'identifiant du document.
     * @param download Indique si le fichier doit être téléchargé (true) ou affiché (false par défaut).
     * @return Le document en tant que flux de données.
     */
    @GetMapping(BaConstants.URL.DOCUMENT + "/telecharger/autorisation/{idDoc}")
    public ResponseEntity<byte[]> lireOuTelechargerDocument(
            @PathVariable final String idDoc,
            @RequestParam(name = "download", defaultValue = "true") boolean download) {

        // Récupérer le document en base
        BaDocumentAutorisationSpecial document = documentAutorisationRepository.findById(idDoc)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Document introuvable avec l'ID : " + idDoc));

        // Lire le contenu du fichier via le service existant
        byte[] fichier = baFileStorageService.getDocumentAutorisationSpeciale(idDoc);

        // Déterminer le nom du fichier
        String nomFichier = document.getLibelle() != null ? document.getLibelle() + ".pdf" : "document.pdf";

        // Définir les en-têtes HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Si téléchargement demandé → content-disposition: attachment
        if (download) {
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(nomFichier).build());
        } else {
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(nomFichier).build());
        }

        return new ResponseEntity<>(fichier, headers, HttpStatus.OK);
    }



    /**
     * Fonction permettant de retourner un tableau de Byte d'un document pour une autorisation spéciale
     * @param idDoc : id de document
     * @return un tableau de Byte
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/lecture/membre/{idDoc}")
    public ResponseEntity<byte[]> lectureDocument(@PathVariable final String idDoc) {
        return new ResponseEntity<>(baFileStorageService.getDocumentMembreAutorisationSpeciale(idDoc), HttpStatus.OK);
    }
    /**
     * Endpoint pour téléverser le document final lié à une autorisation spéciale validée.
     * Ce document est envoyé par un administrateur après validation.
     * Exemple d'URL : POST /api/delegation-speciale/autorisation/{id}/document-final
     *
     * @param id L'identifiant de l'autorisation spéciale.
     * @param file Le fichier PDF à téléverser comme document final.
     * @return Le document final ajouté sous forme de DTO.
     */
    @PostMapping(BaConstants.URL.AUTORISATION + "/document-final/{id}")
    public ResponseEntity<BaDocumentAutorisationSpecialDto> uploadDocumentFinal(
            @PathVariable("id") String id,
            @Valid @RequestPart("file") MultipartFile file) {

        BaDocumentAutorisationSpecialDto documentDto = autorisationService.uploadDocumentFinal(id, file);
        return ResponseEntity.ok(documentDto);
    }

    /**
     * Endpoint pour supprimer le document final lié à une autorisation spéciale.
     * Exemple d'URL : DELETE /api/delegation-speciale/autorisation/{id}/document-final
     *
     * @param id L'identifiant de l'autorisation spéciale.
     * @return Un message de succès si la suppression est réussie.
     */
    @DeleteMapping(BaConstants.URL.AUTORISATION + "/document-final/{id}")
    public ResponseEntity<String> deleteDocumentFinal(@PathVariable("id") String id) {
        autorisationService.deleteDocumentFinal(id);
        return ResponseEntity.ok("Le document final a été supprimé avec succès.");
    }



}
