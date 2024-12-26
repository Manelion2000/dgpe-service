package com.bakouan.app.controller;

import com.bakouan.app.dto.*;
import com.bakouan.app.enums.ETypeDemandeur;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.service.BaParamService;
import com.bakouan.app.utils.BaConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
public class ParamController {

    private final BaParamService paramService;
    private final BaFileStorageService fileStorage;


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
     * Récupérer toutes les demandes.
     *
     * @return liste de {@link BaDemandeDto}
     */
    @GetMapping(BaConstants.URL.DEMANDE)
    public List<BaDemandeDto> getAllDemandes() {
        return paramService.getAllDemandes();
    }

    /**
     * Récupérer toutes les demandes archivées.
     *
     * @return liste de {@link BaDemandeDto}
     */
    @GetMapping(BaConstants.URL.DEMANDE + "/archive")
    public List<BaDemandeDto> getAllDemandesArchive() {
        return paramService.getAllDemandesArchive();
    }

    /**
     * Récupérer une demande par son identifiant.
     *
     * @param id l'identifiant de la demande
     * @return {@link ResponseEntity} contenant la demande
     */
    @GetMapping(BaConstants.URL.DEMANDE + "/{id}")
    public ResponseEntity<BaDemandeDto> getDemandeById(@PathVariable final String id) {
        return ResponseEntity.ok(paramService.getDemandeByid(id));
    }

    /**
     * Récupérer la liste des demandes en fonction du type de demandeur.
     *
     * @param typeDemandeur : le type du demandeur
     * @return {@link ResponseEntity} contenant la liste des demandes
     */
    @GetMapping(BaConstants.URL.DEMANDE + "/{typeDemandeur}")
    public ResponseEntity<List<BaDemandeDto>> getDemandeByTyDemandeur(@PathVariable final ETypeDemandeur typeDemandeur) {
        List<BaDemandeDto> demandes = paramService.getDemandeTypeDemandeur(typeDemandeur);
        return ResponseEntity.ok(demandes);
    }


    /**
     * Créer une nouvelle demande.
     *
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande créée
     */
    @PostMapping(BaConstants.URL.DEMANDE)
    public ResponseEntity<BaDemandeDto> createDemande(@RequestBody @Valid final BaDemandeDto demandeDto) {
        final BaDemandeDto createdDemande = paramService.createDemande(demandeDto);
        return new ResponseEntity<>(createdDemande, HttpStatus.CREATED);
    }

    /**
     * Retourne la liste des demandes rejetées
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/rejetees")
    public List<BaDemandeDto> getDemandesRejetees() {
        return paramService.getDemandeRejette();
    }

    /**
     * Retourne la liste des demandes en cours
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/encours")
    public List<BaDemandeDto> getDemandesEnCours() {
        return paramService.getDemandeEncours();
    }

    /**
     * Retourne la liste des demandes produites
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/produites")
    public List<BaDemandeDto> getDemandesProduites() {
        return paramService.getDemandeProduit();
    }

    /**
     * Retourne la liste des demandes retirées (délivrées)
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/retirees")
    public List<BaDemandeDto> getDemandesRetirees() {
        return paramService.getDemandeRetirer();
    }

    /**
     * Retourne la liste des demandes de cartes d'accès aux salons officiels
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/cartes-salon-officiel")
    public List<BaDemandeDto> getDemandesSalonOfficiel() {
        return paramService.getDemandeSalonOfficiel();
    }

    /**
     * Retourne la liste des demandes de cartes diplomatiques
     * @return Liste de BaDemandeDto
     */
    @GetMapping(BaConstants.URL.DEMANDE+"/cartes-diplomatiques")
    public List<BaDemandeDto> getDemandesCartesDiplomatiques() {
        return paramService.getDemandeSalonDiplomatique();
    }

    /**
     * Mettre à jour une demande existante.
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails mis à jour de la demande
     * @return {@link ResponseEntity} contenant la demande mise à jour
     */
    @PutMapping(BaConstants.URL.DEMANDE + "/{id}")
    public ResponseEntity<BaDemandeDto> updateDemande(@PathVariable final String id, @RequestBody @Valid final BaDemandeDto demandeDto) {
        final BaDemandeDto updatedDemande = paramService.updateDemande(id, demandeDto);
        return ResponseEntity.ok(updatedDemande);
    }

    /**
     * Valider une demande existante par le service technique du DGPE
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande validée
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/valider/{id}")
    public ResponseEntity<BaDemandeDto> validerDemande(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.validerDemande(id, demandeDto));
    }

    /**
     * Produire une carte (carte produite par la DSI)
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande produite
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/produire/{id}")
    public ResponseEntity<BaDemandeDto> produireDemande(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.produireDemande(id, demandeDto));
    }

    /**
     * Marquer une carte à RETIRER par le service technique du DGPE
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande validée
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/retirer/{id}")
    public ResponseEntity<BaDemandeDto> retirerDemande(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.retirerDemande(id, demandeDto));
    }

    /**
     * Valider une demande existante par le DGPE
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande validée
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/valider_dg/{id}")
    public ResponseEntity<BaDemandeDto> validerDemandeDg(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.validerDemandeParDg(id, demandeDto));
    }

    /**
     * Rejeter une demande existante.
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande rejetée
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/rejeter/{id}")
    public ResponseEntity<BaDemandeDto> rejeterDemande(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.rejeterDemande(id, demandeDto));
    }

    /**
     * Rejeter une demande existante.
     *
     * @param id l'identifiant de la demande
     * @param demandeDto les détails de la demande
     * @return {@link ResponseEntity} contenant la demande rejetée
     */
    @PatchMapping(BaConstants.URL.DEMANDE + "/rejeter_dg/{id}")
    public ResponseEntity<BaDemandeDto> rejeterDemandeParDg(@PathVariable final String id, @RequestBody final BaDemandeDto demandeDto) {
        return ResponseEntity.ok(paramService.rejeterDemandeParDg(id, demandeDto));
    }

    /**
     * Ajouter un document à une demande.
     *
     * @param idDemande l'identifiant de la demande
     * @param file le fichier à télécharger
     * @param documentDto les détails du document
     * @return {@link ResponseEntity} avec le statut de création
     */
    @PostMapping(value = BaConstants.URL.DOCUMENT + "/{idDemande}",consumes = {"multipart/form-data"})
    public ResponseEntity<Void> addDocument(
            @PathVariable final String idDemande,
            @RequestPart("file") final MultipartFile file,
            @RequestPart("documentDto") @Valid final BaDocumentDto documentDto) {
        paramService.saveDocument(file, documentDto, idDemande);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Récupérer les documents d'une demande.
     *
     * @param idDemande l'identifiant de la demande
     * @return liste de {@link BaDocumentDto}
     */
    @GetMapping(BaConstants.URL.DOCUMENT + "/demande/{idDemande}")
    public List<BaDocumentDto> getDocumentsByDemande(@PathVariable final String idDemande) {
        return paramService.getDocumentsByDemande(idDemande);
    }

    /**
     * Suppression logique d'un document.
     *
     * @param id l'identifiant du document
     * @return {@link ResponseEntity} sans contenu
     */
    @DeleteMapping(BaConstants.URL.DOCUMENT + "/logic/{id}")
    public ResponseEntity<Void> deleteDocumentLogique(@PathVariable final String id) {
        paramService.deleteDocumentLogique(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Suppression physique d'un document.
     *
     * @param id l'identifiant du document
     * @return {@link ResponseEntity} sans contenu
     */
    @DeleteMapping(BaConstants.URL.DOCUMENT + "/{id}")
    public ResponseEntity<Void> deleteDocumentPhysique(@PathVariable final String id) {
        paramService.deletePhysique(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Mettre à jour un document existant.
     *
     * @param documentId l'identifiant du document
     * @param documentDto les détails du document
     * @return {@link ResponseEntity} contenant le document mis à jour
     */
    @PutMapping(BaConstants.URL.DOCUMENT + "/{documentId}")
    public ResponseEntity<BaDocumentDto> updateDocument(
            @PathVariable final String documentId,
            @RequestBody @Valid final BaDocumentDto documentDto) {
        final BaDocumentDto updatedDocument = paramService.updateDocument(documentId, documentDto);
        return ResponseEntity.ok(updatedDocument);
    }

    /**
     * Fonction permettant de retourner un tableau de Byte d'un document
     * @param idDoc : id de document
     * @return un tableau de Byte
     */

    @GetMapping(BaConstants.URL.DOCUMENT + "/lecture/{idDoc}")
    public ResponseEntity<byte[]> lireDocumentContribution(@PathVariable final String idDoc) {
        return new ResponseEntity<>(fileStorage.getDocument(idDoc), HttpStatus.OK);
    }

    /**
     * Crée une mission diplomatique.
     *
     * @param missionDto les détails de la mission.
     * @return la mission créée.
     */
    @PostMapping(BaConstants.URL.MISSION)
    public ResponseEntity<BaMissionDiplomatiqueDto> createMission(final @Valid @RequestBody BaMissionDiplomatiqueDto missionDto) {
        return new ResponseEntity<>(paramService.createMission(missionDto), HttpStatus.CREATED);
    }

    /**
     * Met à jour une mission diplomatique.
     *
     * @param id         l'identifiant de la mission à mettre à jour.
     * @param missionDto les nouveaux détails de la mission.
     * @return la mission mise à jour.
     */
    @PutMapping(BaConstants.URL.MISSION +"/{id}")
    public ResponseEntity<BaMissionDiplomatiqueDto> updateMission(@PathVariable final String id,final @Valid  @RequestBody BaMissionDiplomatiqueDto missionDto) {
        return new ResponseEntity<>(paramService.updateMission(id, missionDto), HttpStatus.OK);
    }

    /**
     * Archive une mission diplomatique.
     * @param id l'identifiant de la mission à archiver.
     */
    @PatchMapping(BaConstants.URL.MISSION+"/id")
    public ResponseEntity<Void> archiveMission(@PathVariable final String id) {
        paramService.archiveMission(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retourne la liste de toutes les missions diplomatiques.
     *
     * @return la liste des missions.
     */
    @GetMapping(BaConstants.URL.MISSION)
    public ResponseEntity<List<BaMissionDiplomatiqueDto>> getAllMissions() {
        return new ResponseEntity<>(paramService.getAllMissions(), HttpStatus.OK);
    }

    /**
     * Retourne une mission diplomatique par son identifiant.
     *
     * @param id l'identifiant de la mission à récupérer.
     * @return la mission trouvée.
     */
    @GetMapping(BaConstants.URL.MISSION +"/{id}")
    public ResponseEntity<BaMissionDiplomatiqueDto> getMissionById(@PathVariable final String id) {
        return new ResponseEntity<>(paramService.getMissionById(id), HttpStatus.OK);
    }

    /**
     * Fonction de création d'un personnel
     * @param file        : fichier
     * @param personnelDto DTO Document
     * @return BaPersonneDgpeDto
     */
    @PostMapping(BaConstants.URL.PERSONNEL)
    public ResponseEntity<BaPersonneDgpeDto> savePersonnel(
            @RequestPart("file") final MultipartFile file,
            @RequestPart("personnelDto") final @Valid BaPersonneDgpeDto personnelDto) {

        try {
            BaPersonneDgpeDto savedDto = paramService.savePersonnel(file, personnelDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Met à jour un personnel.
     * @param id: id de l'utilisateur
     * @param personnelDto: personnelDto
     * @return une Dto de mise de personnel
     */
    @PutMapping(BaConstants.URL.PERSONNEL+"/{id}")
    public ResponseEntity<BaPersonneDgpeDto> updatePersonnel(
            @PathVariable("id") final String id,
            @RequestBody final BaPersonneDgpeDto personnelDto) {
        BaPersonneDgpeDto updatedDto = paramService.updatePersonnel(id, personnelDto);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Met à jour un personnel.
     * @param id: id de l'utilisateur
     * @param personnelDto: personnelDto
     * @return une Dto de mise de personnel
     */
    @PatchMapping(BaConstants.URL.PERSONNEL+"/{id}")
    public ResponseEntity<BaPersonneDgpeDto> archiverPersonnel(
            @PathVariable("id") final String id,
            @RequestBody final BaPersonneDgpeDto personnelDto) {
        BaPersonneDgpeDto updatedDto = paramService.archiverPersonnel(id, personnelDto);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Supprime un personnel.
     * @param id: id du personnel
     * @return rien
     */
    @DeleteMapping(BaConstants.URL.PERSONNEL+"/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable("id") final String id) {
        paramService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère tous les personnels
     * @return une liste de personnel
     */
    @GetMapping(BaConstants.URL.PERSONNEL)
    public ResponseEntity<List<BaPersonneDgpeDto>> getAllPersonnel() {
        List<BaPersonneDgpeDto> personnelList = paramService.getAllPersonnel();
        return ResponseEntity.ok(personnelList);
    }

    /**
     * Récupère tous les personnels
     * @return une liste de personnel archivé
     */
    @GetMapping(BaConstants.URL.PERSONNEL+"/archive")
    public ResponseEntity<List<BaPersonneDgpeDto>> getAllPersonnelArchive() {
        List<BaPersonneDgpeDto> personnelList = paramService.getAllPersonnelArchiver();
        return ResponseEntity.ok(personnelList);
    }

    /**
     * Récupère un personnel par ID.
     * @param id: id de l'utilisateur
     * @return un personnel
     */
    @GetMapping(BaConstants.URL.PERSONNEL+"/{id}")
    public ResponseEntity<BaPersonneDgpeDto> getPersonnelById(@PathVariable("id") final String id) {
        BaPersonneDgpeDto personnelDto = paramService.getPersonnelById(id);
        return ResponseEntity.ok(personnelDto);
    }

    /**
     * Fonction permettant de retourner un tableau de Byte du personnel
     * @param idDoc : id de document du personnel
     * @return un tableau de Byte
     */

    @GetMapping(BaConstants.URL.PERSONNEL + "/lecture/{idDoc}")
    public ResponseEntity<byte[]> lireDocumentPersonnel(@PathVariable final String idDoc) {
        return new ResponseEntity<>(fileStorage.getPhotoPersonnel(idDoc), HttpStatus.OK);
    }
}
