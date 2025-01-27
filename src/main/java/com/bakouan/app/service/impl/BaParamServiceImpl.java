package com.bakouan.app.service.impl;

import com.bakouan.app.dto.*;
import com.bakouan.app.enums.*;
import com.bakouan.app.mapper.YtMapper;
import com.bakouan.app.model.*;
import com.bakouan.app.repositories.*;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.service.BaLogService;
import com.bakouan.app.service.BaMailService;
import com.bakouan.app.service.BaParamService;
import com.bakouan.app.utils.BaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class BaParamServiceImpl implements BaParamService {

    private final BaDemandeRepository baDemandeRepository;
    private final BaDocumentRepository baDocumentRepository;
    private  final BaFileStorageService baFileStorageService;
    private final BaUserRepository baUserRepository;
    private final BaMissionDiplomatiqueRepository missionDiplomatiqueRepository;
    private final BaPersonnelRepository baPersonnelRepository;
    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);
    private final BaLogService logService;
    private final BaMailService mailService;


@Override
public List<BaDemandeDto> getAllDemandes() {
        logService.log(new BaLogDto(EAction.V, "Démandes"));
        return baDemandeRepository.findByStatut(EStatut.A)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction aui retournant la liste de demandes archivées
     * @return
     */
    @Override
public List<BaDemandeDto> getAllDemandesArchive() {
        logService.log(new BaLogDto(EAction.V, "Démandes"));
        return baDemandeRepository.findByStatut(EStatut.D)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction prmettant de retourner la liste de demande validée
     * @return
     */
    @Override
    public List<BaDemandeDto> getDemandeValider() {
        return baDemandeRepository.findByStatus(EStatus.VALIDER)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDemandeDto> getDemandeValiderDg() {
        return baDemandeRepository.findByStatus(EStatus.VALIDER_DG)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction permettant de retourner la liste des documents
     * @return
     */

    @Override
    public List<BaDemandeDto> getDemandeRejette() {
        return baDemandeRepository.findByStatus(EStatus.REJETER)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDemandeDto> getDemandeEncours() {
        return baDemandeRepository.findByStatus(EStatus.ENCOURS)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDemandeDto> getDemandeProduit() {
        return baDemandeRepository.findByStatus(EStatus.PRODUIT)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }
    @Override
    public List<BaDemandeDto> getDemandeRetirer() {
        return baDemandeRepository.findByStatus(EStatus.DELIVRE)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction permettant de retourner la liste de demande des cartes diplomatiques
     * @return {@Link une list de demande}
     */
    @Override
    public List<BaDemandeDto> getDemandeSalonOfficiel() {
        return baDemandeRepository.findByECarte(ECarte.CARTE_ACCES)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction permettant de retourner une liste en fonction de type de demandeur
     * @return {@Link une liste de demande}
     */
    @Override
    public List<BaDemandeDto> getDemandeTypeDemandeur(ETypeDemandeur typeDemandeur) {
        return baDemandeRepository.findByTypeDemandeur(typeDemandeur)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction prmettant de retourner la liste de demande des cartes diplomatiques
     * @return {@Link une list de demande}
     */
    @Override
    public List<BaDemandeDto> getDemandeSalonDiplomatique() {
        return baDemandeRepository.findByECarte(ECarte.CARTE_DIPLOMATIQUE)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction permettant de rétourner une demande par son id
     * @param id: id de l'utilisateur
     * @return {Link BaDemandeDto  }
     */

@Override
public BaDemandeDto getDemandeByid(String id) {
        logService.log(new BaLogDto(EAction.V, "Démandes"));
        return baDemandeRepository.findById(id).map(mapper::maps)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, " cette démande n'existe pas"));
    }

    /**
     * Fonction de création d'une démande
     * @param demandeDto: documentDTo
     * @return
     */

    @Override
    public BaDemandeDto createDemande(final BaDemandeDto demandeDto) {
        logService.log(new BaLogDto(EAction.C, "Création de la demande " + demandeDto.getNumeroDemande()));

        BaDemande demande = mapper.maps(demandeDto);
        // Vérification de l'existence de la mission diplomatique
        BaMissionDiplomatique mission=missionDiplomatiqueRepository.findById(demandeDto.getIdMissionDiplomatique())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"la mission diplomatique est introuvable "));

        // Vérification de l'existence de l'utilisateur
        BaUser user=baUserRepository.findById(demandeDto.getIdUser())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"l'utilisateur introuvable "));


        demande.setId(BaUtils.randomUUID());
        demande.setDateDemande(LocalDate.now());
        demande.setMissionDiplomatique(mission);
        demande.setUser(user);
        demande.setStatus(EStatus.ENCOURS);
        BaDemande savedDemande = baDemandeRepository.save(demande);
        return mapper.maps(savedDemande);
    }
    /**
     * Fonction de la mise à jour d'une demande
     * @param id: id de la demande
     * @param demandeDto demande Dto
     * @return { @Link BaDemandeDto}
     */

    @Override
    public BaDemandeDto updateDemande(String id, BaDemandeDto demandeDto) {
        logService.log(new BaLogDto(EAction.U, "Mise à jour de demande " + demandeDto.getNumeroDemande()));

        BaDemande demande = baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));


        BaUser user = baUserRepository.findById(demandeDto.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur  n'existe pas"));
        demande.setUser(user);
        demande.setStatus(EStatus.ENCOURS);
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        return mapper.maps(updatedDemande);

    }

    /**
     * Fonction de validation d'une demande
     * @param id: id de demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto validerDemande(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation du service technique" + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette immatriculation n'existe pas"));

        demande.setStatus(EStatus.VALIDER);
        demande.setTypeCarte(demandeDtoDto.getTypeCarte());
        demande.setTypeCarteAcces(demandeDtoDto.getTypeCarteAcces());
        demande.setDateValidation(LocalDate.now());
        // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
//        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
//                "Merci votre demande viens d'être validée par le service compétante pour motif :\n "+demande.getMotifRejet(),"Demande d'immatriculation");
        return mapper.maps(updatedDemande);


    }

    /**
     * Fonction de validation du DG d'une demande
     * @param id: id de demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto validerDemandeParDg(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation de la demande " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.VALIDER_DG);
        demande.setDateValidationDg(LocalDate.now());
        // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Votre demande acceptée par le Directeur General du Protocole d'Etat :\n "+demande.getMotifRejet(),"Demande d'immatriculation");
        return mapper.maps(updatedDemande);


    }
    /**
     * Fonction permettant e changer l'état de la carte à PRODUITE d'une demande
     * @param id: id de demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto produireDemande(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation de la demande " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.PRODUIT);
        demande.setDateProduction(LocalDate.now());
        // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Votre carte est prêtre et vous vous passer la récuperer à la Direction Général du " +
                        "protocole:\n "+demande.getNumeroDemande(),"Demande de carte");
        return mapper.maps(updatedDemande);


    }

    /**
     * Fonction permettant e changer l'état de la carte à RETIRER d'une demande
     * @param id: id de demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto retirerDemande(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation de la demande " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.PRODUIT);
        demande.setDateRetrait(LocalDate.now());
        // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        return mapper.maps(updatedDemande);

    }
    /**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto rejeterDemande(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.REJETER);
        demande.setDateValidation(LocalDate.now());
        demande.setMotifRejet(demande.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Desolé votre demande vient d'être rejeté pour motif :\n "+demande.getMotifRejet(),"Demande d'immatriculation");
        return mapper.maps(updatedDemande);


    }

    /**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto rejeterDemandeParDg(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande par le DGPE " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.REJETER_DG);
        demande.setDateValidationDg(LocalDate.now());
        demande.setMotifRejet(demande.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Desolé votre demande vient d'être rejeté pour motif :\n "+demande.getMotifRejet(),"Demande d'immatriculation");
        return mapper.maps(updatedDemande);


    }

    /**
     * Fonction d'ajout de document
     *
     * @param file   : fichier
     * @param documentDto DTO Document
     * @param idDemande: id de la demande
     */

    @Override
    public BaDocument saveDocument(MultipartFile file, BaDocumentDto documentDto, String idDemande) {
        // Enregistrement du fichier
        String filePath = baFileStorageService.saveFile(file);

        // Création du libellé avec type de document et numéro de document
        String libelle = String.format("%s_%s", documentDto.getTypeDocument().name(), documentDto.getNumDocument());
        BaDemande demande=baDemandeRepository.findById(idDemande).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"la demande est introvable"));

        // Création du document avec les détails du DTO
        BaDocument maDocument = new BaDocument();
        maDocument.setId(BaUtils.randomUUID());
        maDocument.setLibelle(libelle);
        maDocument.setUrl(filePath);
        maDocument.setTypeDocument(documentDto.getTypeDocument());
        maDocument.setDemande(demande);
        return baDocumentRepository.save(maDocument);

    }

    /**
     * Fonction permettant de retourner la liste des documents par demande.
     *
     * @param idDemande : l'id de l
     */
    @Override
    public List<BaDocumentDto> getDocumentsByDemande(String idDemande) {
        return baDocumentRepository.findByDemandeId(idDemande)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Supprime un document d'une demande.
     *
     * @param demandeId l'ID de l'immatriculation
     * @param documentId        l'ID du document à supprimer
     */
    @Override
    public void removeDocumentFromDemande(String demandeId, String documentId) {
        logService.log(new BaLogDto(EAction.D, "Suppression d'un document " + documentId + " de l'immatriculation " + demandeId));

        // Vérifier l'existence de l'immatriculation
        BaDemande demande = baDemandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Demande introuvable"));

        // Vérifier l'existence du document
        BaDocument document = baDocumentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document introuvable"));

        // Vérifier que le document est associé à l'immatriculation
        if (!demande.getDocuments().contains(document)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le document n'est pas associé à cette demande");
        }

        // Supprimer le document de l'ensemble des documents
        demande.getDocuments().remove(document);

        // Supprimer le document de la base de données si nécessaire
        baDocumentRepository.delete(document);

        // Sauvegarder l'immatriculation mise à jour
        baDemandeRepository.save(demande);

        logService.log(new BaLogDto(EAction.D, "Document supprimé avec succès de la demande"));
    }

    /**
     * Fonction de suppression logique (archivage) d'un document.
     *
     * @param id
     */
    @Override
    public void deleteDocumentLogique(final String id) {
        logService.log(new BaLogDto(EAction.D, "Archivage de d'un document d'une demande " + id));
        BaDocument document = baDocumentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce document n'existe pas"));
        document.setStatut(EStatut.D);  // Archiver l'immatriculation
        baDocumentRepository.save(document);
    }


    /**
     * Fonction de suppression physique d'un document d'immatriculation.
     *
     * @param id
     */
    @Override
    public void deletePhysique(final String id) {
        logService.log(new BaLogDto(EAction.D, "Suppression d'un document " + id));
        BaDocument document = baDocumentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce document n'existe pas"));
        baDocumentRepository.delete(document);
    }

    /**
     * Fonction de mise à jour d'un document
     * @param documentId: document Id
     * @param documentDto: documentDTo
     * @return
     */
    @Override
    public BaDocumentDto updateDocument(String documentId, BaDocumentDto documentDto) {
        BaDocument document = baDocumentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvé"));

        document.setLibelle(String.format("%s_%s", documentDto.getTypeDocument().name(), documentDto.getNumDocument()));
        document.setTypeDocument(documentDto.getTypeDocument());
        BaDocument updatedDocument=baDocumentRepository.save(document);

        return mapper.maps(updatedDocument);
    }

    /**
     * Crée une nouvelle mission diplomatique.
     *
     * @param missionDto les détails de la mission à créer.
     * @return la mission créée.
     */
    @Override
    public BaMissionDiplomatiqueDto createMission(BaMissionDiplomatiqueDto missionDto) {
        logService.log(new BaLogDto(EAction.C, "Création de la mission diplomatique " + missionDto.getLibelle()));
        BaMissionDiplomatique mission = mapper.maps(missionDto);
        mission.setId(BaUtils.randomUUID());
        BaMissionDiplomatique savedMission = missionDiplomatiqueRepository.save(mission);
        return mapper.maps(savedMission);
    }

    /**
     * Met à jour une mission diplomatique existante.
     *
     * @param id         l'identifiant de la mission à mettre à jour.
     * @param missionDto les nouveaux détails de la mission.
     * @return la mission mise à jour.
     */
    @Override
    public BaMissionDiplomatiqueDto updateMission(String id, BaMissionDiplomatiqueDto missionDto) {
        logService.log(new BaLogDto(EAction.U, "Mise à jour de la mission diplomatique " + missionDto.getLibelle()));
        BaMissionDiplomatique existingMission = missionDiplomatiqueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission diplomatique non trouvée"));

        existingMission.setLibelle(missionDto.getLibelle());
        existingMission.setPays(missionDto.getPays());
        existingMission.setNationalite(missionDto.getNationalite());

        BaMissionDiplomatique updatedMission = missionDiplomatiqueRepository.save(existingMission);
        return mapper.maps(updatedMission);
    }

    /**
     * Archive une mission diplomatique en la supprimant.
     *
     * @param id l'identifiant de la mission à archiver.
     */
    @Override
    public void archiveMission(String id) {
        logService.log(new BaLogDto(EAction.D, "Archivage de la mission diplomatique " + id));
        BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission diplomatique non trouvée"));
        mission.setStatut(EStatut.D);
        missionDiplomatiqueRepository.delete(mission);
    }

    /**
     * Retourne la liste de toutes les missions diplomatiques.
     *
     * @return la liste des missions diplomatiques.
     */
    @Override
    public List<BaMissionDiplomatiqueDto> getAllMissions() {
        return missionDiplomatiqueRepository.findAll().stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Retourne une mission diplomatique par son identifiant.
     *
     * @param id l'identifiant de la mission à récupérer.
     * @return la mission correspondante.
     */
    @Override
    public BaMissionDiplomatiqueDto getMissionById(String id) {
        BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission diplomatique non trouvée"));
        return mapper.maps(mission);
    }

    /**
     * Fonction de création d'un document rapport HCBE.
     *
     * @param file        : fichier de a contribution
     * @param personnelDto DTO Document
     */
    @Override
    public BaPersonneDgpeDto savePersonnel(final MultipartFile file, final BaPersonneDgpeDto personnelDto) {
        // Enregistrement du fichier
        String filePath = baFileStorageService.saveFile(file);
        //String fichier=file.getOriginalFilename();
        // Création du document avec les détails du DTO
        BaPersonnelDgpe personnel = mapper.maps(personnelDto);

        personnel.setId(BaUtils.randomUUID());
        personnel.setUrl(filePath);
        BaPersonnelDgpe saved = baPersonnelRepository.save(personnel);

        return mapper.maps(saved);

    }


    /**
     * Met à jour un personnel existant.
     */
    @Override
    public BaPersonneDgpeDto updatePersonnel(final String id, final BaPersonneDgpeDto personnelDto) {
        BaPersonnelDgpe existingPersonnel = baPersonnelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Personnel non trouvé avec l'ID : " + id));

        // Mettre à jour les champs nécessaires
        existingPersonnel.setLibelle(personnelDto.getLibelle());
        existingPersonnel.setFonction(personnelDto.getFonction());
        existingPersonnel.setParagraphe1(personnelDto.getParagraphe1());
        existingPersonnel.setParagraphe2(personnelDto.getParagraphe2());
        existingPersonnel.setParagraphe3(personnelDto.getParagraphe3());
        existingPersonnel.setParagraphe4(personnelDto.getParagraphe4());

        BaPersonnelDgpe updated = baPersonnelRepository.save(existingPersonnel);

        return mapper.maps(updated);
    }

    /**
     * Fonction d'archivage d'un personnel
     * @param id: id du personnel
     * @param personnelDto: personnel DTO
     * @return une personne mise à jour
     */
    @Override
    public BaPersonneDgpeDto archiverPersonnel(final String id, final BaPersonneDgpeDto personnelDto) {
        BaPersonnelDgpe existingPersonnel = baPersonnelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Personnel non trouvé avec l'ID : " + id));

        // Mettre à jour les champs nécessaires
        existingPersonnel.setStatut(EStatut.D);

        BaPersonnelDgpe updated = baPersonnelRepository.save(existingPersonnel);

        return mapper.maps(updated);
    }

    /**
     * Supprime un personnel par ID.
     */
    @Override
    public void deletePersonnel(final String id) {
        baPersonnelRepository.deleteById(id);
    }

    /**
     * Récupère tous les personnels.
     */
    @Override
    public List<BaPersonneDgpeDto> getAllPersonnel() {
        return baPersonnelRepository.findByStatut(EStatut.A).stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les personnels archivés.
     */
    @Override
    public List<BaPersonneDgpeDto> getAllPersonnelArchiver() {
        return baPersonnelRepository.findByStatut(EStatut.D).stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un personnel par ID.
     */
    @Override
    public BaPersonneDgpeDto getPersonnelById(final String id) {
        BaPersonnelDgpe personnel = baPersonnelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Personnel non trouvé avec l'ID : " + id));

        return mapper.maps(personnel);
    }

}
