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
    private final BaPhotoPersonnelRepository baPhotoPersonnelRepository;
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
     * Récupère la liste des demandes pour un utilisateur spécifique.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Liste des demandes de l'utilisateur sous forme de DTO.
     */
    @Override
    public List<BaDemandeDto> getDemandesByUser(String userId) {
        List<BaDemande> demandes = baDemandeRepository.findDemandesByUserId(userId);
        return demandes.stream().map(mapper::maps).collect(Collectors.toList());
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
public List<BaDemandeDto> getDemandesValidOrRejected() {
        List<BaDemande> demandes = baDemandeRepository.findValidOrRejectedAccessCards(
                EStatus.VALIDER,
                EStatus.REJETER,
                ECarte.CARTE_ACCES
        );
        return demandes.stream()
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
    @Override
    public List<BaDemandeDto> getDemandeValiderParDg() {
        return baDemandeRepository.findByStatusDg(EstatusDg.VALIDER)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDemandeDto> getDemandeRejeterDg() {
        return baDemandeRepository.findByStatus(EStatus.REJETER_DG)
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
        return baDemandeRepository.findByDemandeur(typeDemandeur)
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
     * Retourne une liste de demandes par type de carte et statut.
     *
     * @param eCarte le type de carte.
     * @param eStatus le statut des demandes.
     * @return une liste de demandes filtrées par type de carte et statut.
     */
    @Override
    public List<BaDemandeDto> getDemandeParTypeEtStatus(ECarte eCarte, EStatus eStatus) {
        return baDemandeRepository.findDemandeByCarteAndStatus(eCarte, eStatus)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Liste de demande de cartes rejettées (REJETER, REJETER_DG)
     * @param eCarte: type de carte à specifier dans le controller
     * @return : une liste de demande
     */
    @Override
    public List<BaDemandeDto> getDemandesRejectedByDGAndCarte(ECarte eCarte) {
        List<EStatus> rejectedStatuses = List.of(EStatus.REJETER_DG, EStatus.REJETER);
        return baDemandeRepository.findDemandeRejectedByDGAndCarte(ECarte.CARTE_DIPLOMATIQUE, rejectedStatuses)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }


    /**
     * Retourne une liste de demandes par type de carte et statut.
     *
     * @param eStatusDg: stuts de Dg.
     * @param eStatus le statut des demandes.
     * @return une liste de demandes filtrées par type de carte et statut.
     */
    @Override
    public List<BaDemandeDto> getDemandeParStatusEtStatusDg(EStatus eStatus, EstatusDg eStatusDg) {
        return baDemandeRepository.findByStatusAndStatusDg(eStatus, eStatusDg)
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


        // Déterminer le type de carte et compter les demandes existantes
        String typeCarte = (demandeDto.getECarte() == ECarte.CARTE_DIPLOMATIQUE) ? "D" : "A";
        int annee = LocalDate.now().getYear();
       // long sequence = baDemandeRepository.countDemandeByTypeAndYear(demandeDto.getEcarte(), annee) + 1;

        // Générer un numéro de demande unique avec tentative de réessai
        long sequence = baDemandeRepository.countDemandeByTypeAndYear(demandeDto.getECarte(), annee) + 1;
        String numeroDemande;

        do {
            numeroDemande = BaUtils.generateNumeroDemande(typeCarte, sequence);
            sequence++;
        } while (baDemandeRepository.existsByNumeroDemande(numeroDemande));

        demande.setId(BaUtils.randomUUID());
        demande.setNumeroDemande(numeroDemande);
        demande.setDateDemande(LocalDate.now());
        demande.setMissionDiplomatique(mission);
        demande.setUser(user);
        demande.setStatus(EStatus.ENCOURS);
        demande.setECarte(demandeDto.getECarte());
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

        // Vérification de l'existence de la mission diplomatique
        BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(demandeDto.getIdMissionDiplomatique())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La mission diplomatique est introuvable"));

        // Mise à jour des champs de la demande
        demande.setDateDemande(demandeDto.getDateDemande());
        demande.setUser(user);
        demande.setMissionDiplomatique(mission);

        // Mise à jour des informations personnelles
        demande.setNom(demandeDto.getNom());
        demande.setPrenom(demandeDto.getPrenom());
        demande.setDateNaissance(demandeDto.getDateNaissance());
        demande.setLieuNaissance(demandeDto.getLieuNaissance());
        demande.setEmail(demandeDto.getEmail());
        demande.setAdresse(demandeDto.getAdresse());
        demande.setTelephone(demandeDto.getTelephone());
        demande.setProfession(demandeDto.getProfession());
        //demande.setFonction(demandeDto.getFonction());
        demande.setInstitution(demandeDto.getInstitution());
        demande.setNomPrenom(demandeDto.getNomPrenom());
        demande.setTelephoneAprevenir(demandeDto.getTelephoneAprevenir());
        demande.setSexe(demandeDto.getSexe());
        demande.setECarte(demandeDto.getECarte());
        //demande.setStatus(EStatus.ENCOURS);
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        return mapper.maps(updatedDemande);

    }

    /**
     * Fonction de validation d'une demande par le service technique
     * @param id: id de demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto validerDemande(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation du service technique" + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette immatriculation n'existe pas"));

        demande.setStatus(EStatus.VALIDER);
        demande.setDemandeur(demandeDtoDto.getDemandeur());
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

    @Override
    public BaDemandeDto validerDemandeParDG(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Validation de la demande " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatusDg(EstatusDg.VALIDER);
        demande.setDateValidationDg(LocalDate.now());
        // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Votre demande acceptée par le Directeur General du Protocole d'Etat :\n ","Demande de de carte");
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

        demande=mapper.maps(demandeDtoDto);
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
                "Desolé votre demande vient d'être rejeté pour motif :\n "+demande.getMotifRejet(),"Demande de carte");
        return mapper.maps(updatedDemande);


    }/**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto rejeterDemandeParDG(final String id, final BaDemandeDto demandeDtoDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande par le DGPE " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.REJETER_DG);
        demande.setDateValidationDg(LocalDate.now());
        demande.setMotifRejet(demande.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Desolé votre demande vient d'être rejeté pour motif :\n "+demande.getMotifRejet(),"Demande de carte");
        return mapper.maps(updatedDemande);


    }

    /**
     * Fonction d'ajout de document à une demande
     *
     * @param file   : fichier
     * @param documentDto DTO Document
     */

    @Override
    public BaDocument saveDocument(MultipartFile file, BaDocumentDto documentDto) {
        // Enregistrement du fichier
        String filePath = baFileStorageService.saveFile(file);
// Création du document avec les détails du DTO
        BaDocument baDocument = mapper.maps(documentDto);
        // Création du libellé avec type de document et numéro de document
        String libelle = String.format("%s_%s", documentDto.getTypeDocument().name(), documentDto.getNumDocument());
        // Récupérer la demande associée à partir de documentDto (si applicable)
        if (documentDto.getIdDemande() != null) {
            BaDemande demande = baDemandeRepository.findById(documentDto.getIdDemande())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "la demande est introuvable avec l'ID fourni."));
            baDocument.setDemande(demande);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ID de la demande est obligatoire pour enregistrer un document.");
        }
        baDocument.setId(BaUtils.randomUUID());
        baDocument.setLibelle(libelle);
        baDocument.setUrl(filePath);
        baDocument.setTypeDocument(documentDto.getTypeDocument());
        //baDocument.setDemande(demande);
        return baDocumentRepository.save(baDocument);

    }


    @Override
    public BaPhotoPersonnelDto savePhotoPersonnel(MultipartFile file, BaPhotoPersonnelDto photoDto) {
        // Enregistrement du fichier
        String filePath = baFileStorageService.saveFile(file);
// Création du document avec les détails du DTO
        BaPhotoPersonnel baPhoto = mapper.maps(photoDto);
        // Récupérer le personnel associé à partir de documentDto (si applicable)
        if (photoDto.getIdPersonnel() != null) {
            BaPersonnelDgpe personnel = baPersonnelRepository.findById(photoDto.getIdPersonnel())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "le personnel est introuvable avec l'ID fourni."));
            baPhoto.setPersonnel(personnel);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ID du personnel est obligatoire pour enregistrer un document.");
        }
        baPhoto.setId(BaUtils.randomUUID());
        baPhoto.setUrl(filePath);
        baPhoto.setLibelle("photoPersonnel");
        return mapper.maps(baPhotoPersonnelRepository.save(baPhoto));

    }

    /**
     * Supprime un document d'un personnel.
     * @param personnelId: identifiant du personnel
     * @param documentId: identifiant du document
     */
    @Override
    public BaPersonneDgpeDto removeDocumentFromPersonnel(String personnelId, String documentId) {
        logService.log(new BaLogDto(EAction.D, "Suppression d'un document " + documentId + " d'un personnel " + personnelId));

        // Vérifier l'existence de l'immatriculation
        BaPersonnelDgpe personnel = baPersonnelRepository.findById(personnelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "le personnel est introuvable avec l'ID fourni."));;

        // Vérifier l'existence du document
        BaPhotoPersonnel document = baPhotoPersonnelRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document introuvable"));

        // Vérifier que le document est associé à un personnel
        if (!personnel.getDocuments().contains(document)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le document n'est pas associé à ce personnel");
        }

        // Supprimer le document de l'ensemble des documents
        personnel.getDocuments().remove(document);

        // Supprimer le document de la base de données si nécessaire
        baPhotoPersonnelRepository.delete(document);

        // Sauvegarder le personnel mise à jour
         BaPersonnelDgpe personnelDgpe=baPersonnelRepository.save(personnel);
         return mapper.maps(personnelDgpe);

        //logService.log(new BaLogDto(EAction.D, "Document supprimé avec succès de la demande"));
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
    public BaDemandeDto removeDocumentFromDemande(String demandeId, String documentId) {
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

        // Sauvegarder d'une demande mise à jour
       return mapper.maps(baDemandeRepository.save(demande)) ;

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
     * @param personnelDto DTO Document
     */
    @Override
    public BaPersonneDgpeDto savePersonnel(final BaPersonneDgpeDto personnelDto) {

        BaPersonnelDgpe personnel = mapper.maps(personnelDto);
        personnel.setId(BaUtils.randomUUID());
        personnel.setNomPrenom(personnelDto.getNomPrenom());
        personnel.setFonction(personnelDto.getFonction());
        personnel.setParagraphe1(personnelDto.getParagraphe1());
        personnel.setParagraphe2(personnelDto.getParagraphe2());
        personnel.setParagraphe3(personnelDto.getParagraphe3());
        personnel.setParagraphe4(personnelDto.getParagraphe4());
        // personnel.setUrl(filePath);
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
        existingPersonnel.setFonction(personnelDto.getFonction());
        existingPersonnel.setNomPrenom(personnelDto.getNomPrenom());
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


    /**Gestion du reporting (Statisques)
     */


    @Override
    public List<BaStatistiquesDto> getDemandesByStatus() {
        return baDemandeRepository.countDemandesByStatus()
                .stream()
                .map(result -> new BaStatistiquesDto(result[0].toString(), (Long) result[1]))
                .collect(Collectors.toList());
    }
@Override
public List<BaStatistiquesDto> getDemandesByCarte() {
        return baDemandeRepository.countDemandesByCarte()
                .stream()
                .map(result -> new BaStatistiquesDto(result[0].toString(), (Long) result[1]))
                .collect(Collectors.toList());
    }
@Override
public List<BaStatistiquesDto> getDemandesByMonth() {
    return baDemandeRepository.countDemandesByMonth()
            .stream()
            .map(result -> new BaStatistiquesDto(
                    BaUtils.getMonthLabel((Integer) result[0]), // Convertit le numéro du mois en libellé
                    (Long) result[1]
            ))
            .collect(Collectors.toList());
}

    /**
     * Récupère les statistiques globales des demandes.
     *
     * @return BaStatistiqueTotalDto contenant les statistiques globales.
     */

    @Override
    public BaStatistiqueTotalDto getGlobalStatistics() {
        return baDemandeRepository.getGlobalStatistics();
    }

    /**
     * Récupère les statistiques pour un type de carte spécifique.
     *
     * @param eCarte Le type de carte (CARTE_DIPLOMATIQUE ou CARTE_ACCES).
     * @return BaStatistiqueTotalDto contenant les statistiques pour le type de carte.
     */

    @Override
    public BaStatistiqueTotalDto getStatisticsByCarte(ECarte eCarte) {
        return baDemandeRepository.getStatisticsByCarte(eCarte);
    }


}
