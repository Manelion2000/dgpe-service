package accord.gov.app.controller;

import accord.gov.app.dto.*;
import accord.gov.app.service.BaFileStorageService;
import accord.gov.app.service.BaParamService;
import accord.gov.app.utils.BaConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
public class ParamController {

    private final BaParamService paramService;
    private final BaFileStorageService fileStorage;

//========================= GESTION DES TYPES ACCORD==========================
    /**
     * Récupérer un fichier/image/document.
     *
     * @param id : l'id du fichier
     * @return {@link ResponseEntity}
     */
    @GetMapping(BaConstants.URL.DOCUMENT + "/{id}")
    public ResponseEntity<byte[]> loadFile(@PathVariable final String id) {
        return new ResponseEntity<>(fileStorage.get(id), HttpStatus.OK);
    }

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
            @PathVariable String id,
            @Valid @RequestBody BaTypeAccordDto dto) {
        BaTypeAccordDto updated = paramService.updateTypeAccord(id, dto);
        return ResponseEntity.ok(new BaApiResponse<>("Type d’accord mis à jour avec succès", HttpStatus.OK.value(), updated));
    }

    /**
     * Fonction de suppression d’un type d’accord
     * @param id identifiant du type d’accord
     * @return confirmation de suppression
     */
    @DeleteMapping(BaConstants.URL.TYPE_ACCORD + "/{id}")
    public ResponseEntity<BaApiResponse<Void>> deleteTypeAccord(@PathVariable String id) {
        paramService.deleteTypeAccord(id);
        return ResponseEntity.ok(new BaApiResponse<>("Type d’accord supprimé avec succès", HttpStatus.OK.value(), null));
    }
    //========================= GESTION DES LANGUES==========================

    /**
     * Fonction de création d'une langue
     * @param dto DTO de la langue
     * @return BaLangueDto
     */
    @PostMapping(BaConstants.URL.LANGUE)
    public ResponseEntity<BaApiResponse<BaLangueDto>> createLangue(
            @Valid @RequestBody BaLangueDto dto) {
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

}
