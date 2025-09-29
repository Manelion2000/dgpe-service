package accord.gov.app.controller;

import accord.gov.app.dto.*;
import accord.gov.app.enums.EConfidentiel;
import accord.gov.app.enums.EStatut;
import accord.gov.app.model.BaDocument;
import accord.gov.app.model.BaDocumentAffilie;
import accord.gov.app.model.BaFichier;
import accord.gov.app.repositories.BaFichierRepository;
import accord.gov.app.service.BaFileStorageService;
import accord.gov.app.service.BaParamService;
import accord.gov.app.utils.BaConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
@CrossOrigin(origins= {"*"})
public class ParamController {

    private final BaParamService paramService;
    private final BaFileStorageService fileStorage;
    private  final BaFichierRepository fichierRepository;

//========================= GESTION DES TYPES ACCORD==========================

    /**
     * Fonction de création d'un personnel
     * @param dto DTO Document
     * @return BaTypeAccordDto
     */
    @PostMapping(BaConstants.URL.TYPE_ACCORD)
    public ResponseEntity<BaTypeAccordDto> createTypeAccord(@Valid @RequestBody BaTypeAccordDto dto) {
        BaTypeAccordDto created = paramService.createTypeAccord(dto);
        return new ResponseEntity<>(created,HttpStatus.CREATED);
    }

    /**
     * Fonction de récupération d’un type d’accord par son identifiant
     * @param id identifiant du type d’accord
     * @return BaTypeAccordDto
     */
    @GetMapping(BaConstants.URL.TYPE_ACCORD + "/{id}")
    public ResponseEntity<BaApiResponse<BaTypeAccordDto>> getTypeAccordById(@PathVariable String id) {
        BaTypeAccordDto dto = paramService.getAccordById(id);
        return ResponseEntity.ok(new BaApiResponse<>("Type d’accord trouvé", HttpStatus.OK.value(), dto));
    }

    /**
     * Fonction de récupération de tous les types d’accord
     * @return liste des BaTypeAccordDto
     */
    @GetMapping(BaConstants.URL.TYPE_ACCORD)
    public ResponseEntity<List<BaTypeAccordDto>>getAllTypeAccords() {
        List<BaTypeAccordDto> dtos = paramService.getAlTypeAccord();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Fonction de mise à jour d’un type d’accord
     * @param id identifiant du type d’accord
     * @param dto DTO avec les nouvelles données
     * @return BaTypeAccordDto
     */
    @PutMapping(BaConstants.URL.TYPE_ACCORD + "/{id}")
    public ResponseEntity<BaApiResponse<BaTypeAccordDto>> updateTypeAccord(
            @PathVariable final  String id,
            @Valid @RequestBody final BaTypeAccordDto dto) {
        BaTypeAccordDto updated = paramService.updateTypeAccord(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Type d’accord mis à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Fonction de suppression d’un type d’accord
     * @param id identifiant du type d’accord
     * @return confirmation de suppression
     */
    @DeleteMapping(BaConstants.URL.TYPE_ACCORD + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteTypeAccord(@PathVariable final String id) {
        paramService.deleteTypeAccord(id);
        return ResponseEntity.ok(new BaApiResponse<>("Type d’accord supprimé avec succès", HttpStatus.OK.value(), null));
    }
    //========================= GESTION DES TYPES DE DOCUMENTS AFFILIES==========================

    /**
     * Fonction de création d'un type de documents affiliés
     * @param dto DTO du type de document affilié
     * @return BaTypeDocumentAffile
     */
    @PostMapping(BaConstants.URL.TYPE_DOCUMENT_AFF)
    public ResponseEntity<BaTypeDocumentAffilieDto> createTypeDocumentAffilie(
            @Valid @RequestBody final BaTypeDocumentAffilieDto dto) {
        return new ResponseEntity<>(paramService.createTypeDocumentAffilie(dto), HttpStatus.CREATED);
    }

    /**
     * Fonction de récupération d’un type de document par son identifiant
     * @param id identifiant du type de document affilié
     * @return BaTypeDocumentAffilieDto
     */
    @GetMapping(BaConstants.URL.TYPE_DOCUMENT_AFF + "/{id}")
    public ResponseEntity<BaApiResponse<BaTypeDocumentAffilieDto>> getTypeDocumentAffById(@PathVariable final String id) {
        BaTypeDocumentAffilieDto dto = paramService.getTypeDocumentAffilieById(id);
        return ResponseEntity.ok(new BaApiResponse<>("Type de document affilié trouvé", HttpStatus.OK.value(), dto));
    }

    /**
     * Fonction de récupération de tous les types de documents affiliés
     * @return liste des BaTypeDocumentAffDto
     */
    @GetMapping(BaConstants.URL.TYPE_DOCUMENT_AFF)
    public ResponseEntity<List<BaTypeDocumentAffilieDto>> getAllTypeDocAff() {
        List<BaTypeDocumentAffilieDto> dtos = paramService.getAllTypeDocumentAffilie();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Fonction de suppression d’un type d’accord
     * @param id identifiant du type d’accord
     * @return confirmation de suppression
     */
    @PatchMapping(BaConstants.URL.TYPE_DOCUMENT_AFF + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteTypeDodAff(@PathVariable final String id) {
        paramService.deleteTypeDocumentAffilie(id);
        return ResponseEntity.noContent().build();
    }

    //========================= GESTION DES LANGUES=============================================

    /**
     * Fonction de création d'une langue
     * @param dto DTO de la langue
     * @return BaLangueDto
     */
    @PostMapping(BaConstants.URL.LANGUE)
    public ResponseEntity<BaLangueDto> createLangue(
            @Valid @RequestBody final BaLangueDto dto) {
        return new ResponseEntity<>(paramService.createLangue(dto), HttpStatus.CREATED);
    }

    /**
     * Fonction de récupération d’une langue
     * @param id identifiant de la langue
     * @return BaLangueDto
     */
    @GetMapping(BaConstants.URL.LANGUE + "/{id}")
    public ResponseEntity<BaApiResponse<BaLangueDto>> getLangueById(@PathVariable final String id) {
        BaLangueDto dto = paramService.getLangueById(id);
        return ResponseEntity.ok(new BaApiResponse<>("Langue trouvé", HttpStatus.OK.value(), dto));
    }

    /**
     * Fonction de récupération de tous les types d’accord
     * @return liste des BaTypeAccordDto
     */
    @GetMapping(BaConstants.URL.LANGUE)
    public List<BaLangueDto> getAllLangues() {
        return  paramService.getAllLangue();
    }
    /**
     * Fonction de mise à jour d'une langue
     * @param id identifiant de la langue
     * @param dto nouvelles données
     * @return BaLangueDto
     */
    @PutMapping(BaConstants.URL.LANGUE + "/{id}")
    public ResponseEntity<BaLangueDto> updateLangue(
            @PathVariable String id,
            @Valid @RequestBody BaLangueDto dto) {
        return ResponseEntity.ok(paramService.updateTypeAccord(id, dto));
    }

    /**
     * Fonction de suppression d'une langue
     * @param id identifiant de la langue
     * @return confirmation
     */
    @PatchMapping(BaConstants.URL.LANGUE + "/{id}")
    public  ResponseEntity<Void> deleteLangue(@PathVariable String id) {
        paramService.deleteLangue(id);
        return ResponseEntity.noContent().build();
    }
//======================GESTION DES DOMAINES==========================
    /**
     * Fonction de création d’un domaine
     * @param dto DTO du domaine
     * @return BaDomaineDto
     */
    @PostMapping(BaConstants.URL.DOMAINE)
    public ResponseEntity<BaDomaineDto> createDomaine(@Valid @RequestBody BaDomaineDto dto) {
        BaDomaineDto created = paramService.createDomaine(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
}

    /**
     * Fonction de récupération d’un domaine par son ID
     * @param id DTO du domaine par son ID
     * @return BaDomaineDto
     */
    @GetMapping(BaConstants.URL.DOMAINE + "/{id}")
    public ResponseEntity<BaApiResponse<BaDomaineDto>> getDomaineById(@PathVariable String id) {
        BaDomaineDto dto = paramService.getDomaineById(id);
        return ResponseEntity.ok(new BaApiResponse<>("Domaine trouvé", HttpStatus.OK.value(), dto));
    }

    /**
     * Fonction de récupération de tous les domaines
     * @return une Liste de dto de BaDomaineDto
     */
    @GetMapping(BaConstants.URL.DOMAINE)
    public ResponseEntity<List<BaDomaineDto>> getAllDomaines() {
        return new ResponseEntity<>(paramService.getAllDomaine(), HttpStatus.OK);
    }

    /**
     * Fonction de mise à jour d’un domaine
     * @param id DTO du domaine
     *@return BaDomaineDto
     */
    @PutMapping(BaConstants.URL.DOMAINE + "/{id}")
    public ResponseEntity<BaApiResponse<BaDomaineDto>> updateDomaine(
            @PathVariable String id,
            @Valid @RequestBody BaDomaineDto dto) {
        BaDomaineDto updated = paramService.updateDomaine(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Domaine mis à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Fonction de suppression d’un domaine
     *@param id domaine
     *@return BaDomaineDto
     */
    @PatchMapping(BaConstants.URL.DOMAINE + "/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable String id) {
        paramService.deleteDomaine(id);
        return ResponseEntity.noContent().build();
    }

    //========================GESTION DES PARTIES====================

    /**
     * Fonction de création d’une partie prenante
     * @param dto DTO d'une partie
     * @return BaDomaineDto
     */
    @PostMapping(BaConstants.URL.PARTIE)
    public ResponseEntity<BaPartieDto> createPartie(@Valid @RequestBody BaPartieDto dto) {
        return new ResponseEntity<>( paramService.createPartie(dto), HttpStatus.CREATED);
    }

    /**
     * Fonction de récupération d’une partie par son identifiant
     * @param id: identifiant de la partie prenante
     * @return BaPartieDto
     */
    @GetMapping(BaConstants.URL.PARTIE + "/{id}")
    public ResponseEntity<BaApiResponse<BaPartieDto>> getPartieById(@PathVariable String id) {
        BaPartieDto dto = paramService.getPartieById(id);
        return ResponseEntity.ok(new BaApiResponse<>("Partie trouvée", HttpStatus.OK.value(), dto));
    }

    /**
     * Fonction de récupération de toutes les parties
     * @return une List<BaPartie> une liste de toutes les parties prenantes
     */
    @GetMapping(BaConstants.URL.PARTIE)
    public ResponseEntity<List<BaPartieDto>> getAllParties() {
        return ResponseEntity.ok(paramService.getAllPartie());
    }

    /**
     * Fonction de mise à jour d’une partie
     * @param id: identifiant de la partie prenante
     * @return BaPartieDto
     */
    @PutMapping(BaConstants.URL.PARTIE + "/{id}")
    public ResponseEntity<BaPartieDto> updatePartie(
            @PathVariable final String id,
            @Valid @RequestBody final  BaPartieDto dto) {
        BaPartieDto updated = paramService.updatePartie(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /**
     * Fonction de suppression d’une partie
     * @param id identifiant de la partie prenante du domaine
     * @param dto : Dto de la partie
     * @return BaPartie
     */
    @PatchMapping(BaConstants.URL.PARTIE + "/{id}")
    public ResponseEntity<Void> deletePartie(@PathVariable String id, @RequestBody BaPartieDto dto) {
        paramService.deletePartie(id, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fonction de récupération d’une partie par son libellé
     * @param libelle:
     * @param dto: dto partie prenante
     * @return BaPartie
     */
    @GetMapping(BaConstants.URL.PARTIE + "/libelle/{libelle}")
    public ResponseEntity<BaApiResponse<BaPartieDto>> getPartieByLibelle(@PathVariable String libelle,
                                                                         @RequestBody BaPartieDto dto) {
        BaPartieDto partie = paramService.getPartieByLibelle(libelle, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Partie trouvée par libellé", HttpStatus.OK.value(), partie));
    }

    //===========================GESTION DES DOCUMENTS=============================

    /**
     *Création d'un traité ou accord avec les fichiers principaux
     * @param dto: documentDto
     * @param files: la liste des documents principaux
     * @return un Dto de document
     */
    @PostMapping(
            value = BaConstants.URL.DOCUMENT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaDocumentDto> createDocument(
            final @Valid @RequestPart("document") BaDocumentDto dto,
             @RequestPart(value="files", required = true ) List<MultipartFile> files) {
        return ResponseEntity.ok(paramService.createDocument(dto, files));
    }
    /**
     *Création d'un document affilié avec les fichiers principaux
     * @param dto: documentAffilieDto
     * @param file: fichier affilié
     * @return un Dto de document affilié
     */
    @PostMapping(
            value = BaConstants.URL.DOCUMENT_AFF,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaDocumentAffilieDto> createDocumentAffilie(
            final @Valid @RequestPart("document") BaDocumentAffilieDto dto,
             @RequestPart(value="file", required = true ) MultipartFile file) {
        return ResponseEntity.ok(paramService.createDocumentAffilie(dto, file));
    }

    /**
     * Liste des documents actifs
     * @return une liste de documents actifs
     */

    @GetMapping(BaConstants.URL.DOCUMENT)
    public ResponseEntity<List<BaDocumentDto>> getAllDocActive(){
        return ResponseEntity.ok((paramService.getAllDocumentsByStatutAndConfidentialite(EStatut.A, EConfidentiel.NON)));
    }

    /**
     * Fonction de récupération d’un document principale par son identifiant
     * @param id: identifiant du document
     * @return BaDocumentDto
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/{id}")
    public ResponseEntity<BaDocumentDto> getDocumentById(@PathVariable String id) {
        BaDocumentDto dto = paramService.getByDocumentById(id);
        return ResponseEntity.ok(dto);
    }
    /**
     * Liste des documents actifs
     * @return une liste de documents actifs
     */

    @GetMapping(BaConstants.URL.DOCUMENT+"/confidentiel")
    public ResponseEntity<List<BaDocumentDto>> getAllDocActiveEtConfidentiel(){
        return ResponseEntity.ok((paramService.getAllDocumentsByStatutAndConfidentialite(EStatut.A,EConfidentiel.OUI)));
    }
    /**
     * Liste des documents archivés
     * @return une liste de documents archivés
     */

    @GetMapping(BaConstants.URL.DOCUMENT+"/archives")
    public ResponseEntity<List<BaDocumentDto>> getAllDocArchives(){
        return ResponseEntity.ok((paramService.getAllDocumentsByStatutAndConfidentialite(EStatut.D,EConfidentiel.NON)));
    }
    /**
     * Liste des documents confidentiel archivés
     * @return une liste de documents archivés
     */

    @GetMapping(BaConstants.URL.DOCUMENT+"/archives_confidentiel")
    public ResponseEntity<List<BaDocumentDto>> getAllDocConfidentielArchive(){
        return ResponseEntity.ok((paramService.getAllDocumentsByStatutAndConfidentialite(EStatut.D,EConfidentiel.OUI)));
    }


    /**
     * Fonction de mise à jour d’une partie
     * @param id: identifiant de la partie prenante
     * @return BaPartieDto
     */
    @PutMapping(BaConstants.URL.DOCUMENT + "/{id}")
    public ResponseEntity<BaDocumentDto> updateDocument(
            @PathVariable final String id,
            @Valid @RequestBody final  BaDocumentDto dto) {
        BaDocumentDto updated = paramService.updateDocument(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Mise de document (retirer et ajouter des fichiers principaux)
     * @param documentId: id de document
     * @param newFiles: nouveau fichier
     * @param filesToDelete : l'ancien fichier à supprimer
     * @return :
     * @throws IOException:
     */
        @PutMapping(BaConstants.URL.DOCUMENT+"/update/{id}")
        public ResponseEntity<BaDocumentDto> updateDocument(
                @PathVariable("id") String documentId,
                @RequestPart(value = "newFiles", required = false) List<MultipartFile> newFiles,
                @RequestPart(value = "filesToDelete", required = false) List<String> filesToDelete
        ) throws IOException {
            BaDocumentDto updated = paramService.updateDocumentWithFiles(documentId, newFiles, filesToDelete);
            return ResponseEntity.ok(updated);
        }

    /**
     * Récupérer tous les fichiers liés à un document
     * @param idDoc: id du document
     * @return la liste de tous les fichiers
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/fichiers/{idDoc}")
    public ResponseEntity<List<BaFichierDto>> getFichiersByDocument(@PathVariable String idDoc) {
        List<BaFichierDto> fichiers = paramService.getFichiersByDocumentId(idDoc);
        return ResponseEntity.ok(fichiers);
    }
    /**
     * Récupérer tous les fichiers liés à un document affilié
     * @param idDoc: id du document
     * @return la liste de tous les fichiers
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/fichier_affilie/{idDoc}")
    public ResponseEntity<List<BaFichierDto>> getFichiersByDocumentAffilie(@PathVariable String idDoc) {
        List<BaFichierDto> fichiers = paramService.getFichiersByDocumentAffilieId(idDoc);
        return ResponseEntity.ok(fichiers);
    }

    /**
     * Endpoint pour uploader un fichier principal associé à un document existant.
     * Exemple appel (Postman ou Angular) :
     * POST /api/fichiers/upload
     * Content-Type: multipart/form-data
     * Champs :
     *  - file : le fichier (PDF, DOC, etc.)
     *  - fichierDto : JSON contenant { "documentId": "xxxx-uuid", "type": "PRINCIPAL" }
     */
    @PostMapping(value = BaConstants.URL.DOCUMENT+"/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaFichierDto> uploadFichier(
            @RequestPart("file") MultipartFile file,
            @RequestPart("fichierDto") BaFichierDto fichierDto
    ) {
        BaFichierDto saved = paramService.saveFichierPrincipal(file, fichierDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Supprime un fichier associé à un document (accord ou traité).
     *
     * @param fichierId l'ID du personnel.
     * @param documentId l'ID du document.
     * @return l'objet BaPersonneDgpeDto mis à jour.
     */
    @DeleteMapping(BaConstants.URL.DOCUMENT + "/documents/{documentId}/{fichierId}")
    public ResponseEntity<BaDocumentDto> removeFichierFromDocument(@PathVariable final String documentId, @PathVariable final String fichierId) {
        BaDocumentDto updatedDocument= paramService.removeFichierFromAccord(documentId, fichierId);
        return ResponseEntity.ok(updatedDocument);
    }

    /**
     * 🔹 Recherche multicritère de documents
     * Tous les champs du formulaire sont optionnels.
     * Exemple : chercher un traité militaire et sécuritaire bilatéral entre le Burkina et l’Iran
     *
     * @param request Objet contenant les critères de recherche
     * @return Liste des documents correspondant aux critères
     */
    @Operation(summary = "Recherche multicritère", description = "Recherche avancée avec filtres (type, langues, domaines, parties, mots-clés, etc.)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    @PostMapping(BaConstants.URL.DOCUMENT+"/search/multicritere")
    public ResponseEntity<List<BaDocumentDto>> searchDocuments(
            @Valid @RequestBody final BaDocumentSearchRequest request
    ) {
        List<BaDocumentDto> results = paramService.searchMulticritereViaDto(request);
        return ResponseEntity.ok(results);
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
    @GetMapping(BaConstants.URL.DOCUMENT + "/read/{idDoc}")
    public ResponseEntity<byte[]> lireOuTelechargerUnfichierPrincipale(
            @PathVariable final String idDoc,
            @RequestParam(name = "download", defaultValue = "true") boolean download) {

        // 1. Charger le fichier en base
        BaFichier fichier = fichierRepository.findById(idDoc)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Fichier introuvable avec l'ID : " + idDoc));

        // 2. Lire les octets du fichier
        byte[] fileBytes = fileStorage.getFichier(fichier.getUrl());

        // 3. Déterminer le nom du fichier (sinon valeur par défaut)
        String nomFichier = fichier.getLibelle() != null
                ? fichier.getLibelle()
                : "document.pdf";

        // 4. Préparer les headers HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        if (download) {
            headers.setContentDisposition(
                    ContentDisposition.attachment().filename(nomFichier).build()
            );
        } else {
            headers.setContentDisposition(
                    ContentDisposition.inline().filename(nomFichier).build()
            );
        }

        // 5. Retourner la réponse
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    /**
     * Même service que le precedent
     * @param idDoc: id du document
     * @param download;
     * @return un byte
     */
    @GetMapping(BaConstants.URL.DOCUMENT + "/telecharger/{idDoc}")
    public ResponseEntity<byte[]> lire(
            @PathVariable final String idDoc,
            @RequestParam(name = "download", defaultValue = "true") boolean download) {

        return paramService.telechargerFichier(idDoc, download);
    }

    /**
     * Endpoint pour récupérer tous les documents affiliés d’un document principal avec leurs fichiers
     *
     * @param documentId ID du document principal
     * @return Liste de DTOs des documents affiliés avec fichiers
     */
    @GetMapping(BaConstants.URL.DOCUMENT_AFF+"/document/{documentId}")
    public ResponseEntity<List<BaDocumentAffilieDto>> getAffiliesWithFiles(
            @PathVariable String documentId) {
        List<BaDocumentAffilieDto> affilies = paramService.getAllAffiliesWithFiles(documentId);
        return ResponseEntity.ok(affilies);
    }


}