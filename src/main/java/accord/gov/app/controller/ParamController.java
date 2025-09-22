package accord.gov.app.controller;

import accord.gov.app.dto.*;
import accord.gov.app.model.BaDocument;
import accord.gov.app.model.BaTypeDocumentAffilie;
import accord.gov.app.service.BaFileStorageService;
import accord.gov.app.service.BaParamService;
import accord.gov.app.utils.BaConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
public class ParamController {

    private final BaParamService paramService;
    private final BaFileStorageService fileStorage;

//========================= GESTION DES TYPES ACCORD==========================

    /**
     * Fonction de création d'un personnel
     * @param dto DTO Document
     * @return BaTypeAccordDto
     */
    @PostMapping(BaConstants.URL.TYPE_ACCORD)
    public ResponseEntity<BaApiResponse<BaTypeAccordDto>> createTypeAccord(@Valid @RequestBody BaTypeAccordDto dto) {
        BaTypeAccordDto created = paramService.createTypeAccord(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaApiResponse<>("Type d’accord créé avec succès", HttpStatus.CREATED.value(), created));
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
    public ResponseEntity<BaApiResponse<List<BaTypeAccordDto>>> getAllTypeAccords() {
        List<BaTypeAccordDto> dtos = paramService.getAlTypeAccord();
        return ResponseEntity.ok(new BaApiResponse<>("Liste des types d’accords", HttpStatus.OK.value(), dtos));
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
    public ResponseEntity<BaApiResponse<BaTypeDocumentAffilieDto>> createTypeDocumentAffilie(
            @Valid @RequestBody final BaTypeDocumentAffilieDto dto) {
        BaTypeDocumentAffilieDto created = paramService.createTypeDocumentAffilie(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaApiResponse<>("type de document affilié créée avec succès", HttpStatus.CREATED.value(), created));
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
    public ResponseEntity<BaApiResponse<List<BaTypeDocumentAffilieDto>>> getAllTypeDocAff() {
        List<BaTypeDocumentAffilieDto> dtos = paramService.getAllTypeDocumentAffilie();
        return ResponseEntity.ok(new BaApiResponse<>("Liste des types", HttpStatus.OK.value(), dtos));
    }

    /**
     * Fonction de suppression d’un type d’accord
     * @param id identifiant du type d’accord
     * @return confirmation de suppression
     */
    @DeleteMapping(BaConstants.URL.TYPE_DOCUMENT_AFF + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteTypeDodAff(@PathVariable final String id) {
        paramService.deleteTypeDocumentAffilie(id);
        return ResponseEntity.ok(new BaApiResponse<>("Type de document affilié supprimé avec succès", HttpStatus.OK.value(), null));
    }

    //========================= GESTION DES LANGUES=============================================

    /**
     * Fonction de création d'une langue
     * @param dto DTO de la langue
     * @return BaLangueDto
     */
    @PostMapping(BaConstants.URL.LANGUE)
    public ResponseEntity<BaApiResponse<BaLangueDto>> createLangue(
            @Valid @RequestBody final BaLangueDto dto) {
        BaLangueDto created = paramService.createLangue(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaApiResponse<>("Langue créée avec succès", HttpStatus.CREATED.value(), created));
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
    public ResponseEntity<BaApiResponse<List<BaLangueDto>>> getAllLangues() {
        List<BaLangueDto> dtos = paramService.getAllLangue();
        return ResponseEntity.ok(new BaApiResponse<>("Liste des langues", HttpStatus.OK.value(), dtos));
    }
    /**
     * Fonction de mise à jour d'une langue
     * @param id identifiant de la langue
     * @param dto nouvelles données
     * @return BaLangueDto
     */
    @PutMapping(BaConstants.URL.LANGUE + "/{id}")
    public ResponseEntity<BaApiResponse<BaLangueDto>> updateLangue(
            @PathVariable String id,
            @Valid @RequestBody BaLangueDto dto) {
        BaLangueDto updated = paramService.updateTypeAccord(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Langue mise à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Fonction de suppression d'une langue
     * @param id identifiant de la langue
     * @return confirmation
     */
    @DeleteMapping(BaConstants.URL.LANGUE + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteLangue(@PathVariable String id) {
        paramService.deleteLangue(id);
        return ResponseEntity.ok(new BaApiResponse<>("Langue supprimée avec succès", HttpStatus.OK.value(), null));
    }
//======================GESTION DES DOMAINES==========================
    /**
     * Fonction de création d’un domaine
     * @param dto DTO du domaine
     * @return BaDomaineDto
     */
    @PostMapping(BaConstants.URL.DOMAINE)
    public ResponseEntity<BaApiResponse<BaDomaineDto>> createDomaine(@Valid @RequestBody BaDomaineDto dto) {
        BaDomaineDto created = paramService.createDomaine(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaApiResponse<>("Domaine créé avec succès", HttpStatus.CREATED.value(), created));
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
    public ResponseEntity<BaApiResponse<List<BaDomaineDto>>> getAllDomaines() {
        List<BaDomaineDto> dtos = paramService.getAllDomaine();
        return ResponseEntity.ok(new BaApiResponse<>("Liste des domaines", HttpStatus.OK.value(), dtos));
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
    @DeleteMapping(BaConstants.URL.DOMAINE + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteDomaine(@PathVariable String id) {
        paramService.deleteDomaine(id);
        return ResponseEntity.ok(new BaApiResponse<>("Domaine supprimé avec succès", HttpStatus.OK.value(), null));
    }

    //========================GESTION DES PARTIES====================

    /**
     * Fonction de création d’une partie prenante
     * @param dto DTO d'une partie
     * @return BaDomaineDto
     */
    @PostMapping(BaConstants.URL.PARTIE)
    public ResponseEntity<BaApiResponse<BaPartieDto>> createPartie(@Valid @RequestBody BaPartieDto dto) {
        BaPartieDto created = paramService.createPartie(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaApiResponse<>("Partie créé avec succès", HttpStatus.CREATED.value(), created));
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
    public ResponseEntity<BaApiResponse<List<BaPartieDto>>> getAllParties() {
        List<BaPartieDto> dtos = paramService.getAllPartie();
        return ResponseEntity.ok(new BaApiResponse<>("Liste des parties", HttpStatus.OK.value(), dtos));
    }

    /**
     * Fonction de mise à jour d’une partie
     * @param id: identifiant de la partie prenante
     * @return BaPartieDto
     */
    @PutMapping(BaConstants.URL.PARTIE + "/{id}")
    public ResponseEntity<BaApiResponse<BaPartieDto>> updatePartie(
            @PathVariable final String id,
            @Valid @RequestBody final  BaPartieDto dto) {
        BaPartieDto updated = paramService.updatePartie(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Partie mise à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Fonction de suppression d’une partie
     * @param id identifiant de la partie prenante du domaine
     * @param dto : Dto de la partie
     * @return BaPartie
     */
    @DeleteMapping(BaConstants.URL.PARTIE + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deletePartie(@PathVariable String id, @RequestBody BaPartieDto dto) {
        paramService.deletePartie(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Partie supprimée avec succès", HttpStatus.OK.value(), null));
    }

    /**
     * Fonction de récupération d’une partie par son libellé
     * @param libelle
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
     * Fonction de mise à jour d’une partie
     * @param id: identifiant de la partie prenante
     * @return BaPartieDto
     */
    @PutMapping(BaConstants.URL.DOCUMENT + "/{id}")
    public ResponseEntity<BaApiResponse<BaDocumentDto>> updateDocument(
            @PathVariable final String id,
            @Valid @RequestBody final  BaDocumentDto dto) {
        BaDocumentDto updated = paramService.updateDocument(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Document mise à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Mise de document (retirer et ajouter des fichiers principaux)
     * @param documentId: id de document
     * @param newFiles: nouveau fichier
     * @param filesToDelete : l'ancien fichier à supprimer
     * @return :
     * @throws IOException
     */
        @PutMapping("/{id}")
        public ResponseEntity<BaDocumentDto> updateDocument(
                @PathVariable("id") String documentId,
                @RequestPart(value = "newFiles", required = false) List<MultipartFile> newFiles,
                @RequestPart(value = "filesToDelete", required = false) List<String> filesToDelete
        ) throws IOException {
            BaDocumentDto updated = paramService.updateDocumentWithFiles(documentId, newFiles, filesToDelete);
            return ResponseEntity.ok(updated);
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
    }

