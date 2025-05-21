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
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
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
    private final BaCarteRepository baCarteRepository;
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

    /**
     * Fonction permettant de retourner la liste de demande validée
     * @return une liste de demande valide par le DG
     */
    @Override
    public List<BaDemandeDto> getDemandeValiderDg() {
        return baDemandeRepository.findByStatus(EStatus.VALIDER_DG)
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
    public List<BaDemandeDto> getDemandeRejeterDg() {
        return baDemandeRepository.findByStatus(EStatus.REJETER_DG)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDemandeDto> getDemandeAttenteRejeterDg() {
        return baDemandeRepository.findByStatus(EStatus.ATTENTE_REJET_DG)
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
        // Vérification et association de la mission diplomatique uniquement pour CARTE_DIPLOMATIQUE
        BaMissionDiplomatique mission = null;
        if (demandeDto.getECarte() == ECarte.CARTE_DIPLOMATIQUE) {
            mission = missionDiplomatiqueRepository.findById(demandeDto.getIdMissionDiplomatique())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "La mission diplomatique est introuvable"));
            demande.setMissionDiplomatique(mission);
        }

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
        demande.setECarte(demandeDto.getECarte());
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
        if (demande.getStatus() != EStatus.REJETER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La modification est autorisée uniquement si la demande est rejetée ou en cours.");
        }

        BaUser user = baUserRepository.findById(demandeDto.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur  n'existe pas"));
        demande.setUser(user);

        // Vérification de l'existence de la mission diplomatique
       /* BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(demandeDto.getIdMissionDiplomatique())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La mission diplomatique est introuvable"));
*/
        // Mise à jour des champs de la demande
        demande.setDateDemande(demandeDto.getDateDemande());
        demande.setUser(user);

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
        logService.log(new BaLogDto(EAction.U, "Retrait de la demande " + id));

        BaDemande demande = baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas."));

        // Vérifie si la carte a bien été produite avant de permettre le retrait
        if (demande.getStatus() != EStatus.PRODUIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carte n'est pas encore produite. Retrait impossible.");
        }

        // Marquer la demande comme délivrée
        demande.setStatus(EStatus.DELIVRE);
        demande.setDateRetrait(LocalDate.now());

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

        // Récupérer l'entité persistante
        BaDemande demande = baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        // Sinon, on laisse la mission diplomatique existante inchangée (ou on la met à null si souhaité).
        if (demandeDtoDto.getIdMissionDiplomatique() != null) {
            BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(demandeDtoDto.getIdMissionDiplomatique())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La mission diplomatique est introuvable"));
            demande.setMissionDiplomatique(mission);
        }
        // Si le DTO ne fournit pas d'identifiant de mission, vous pouvez décider de garder l'ancienne valeur.
        // Par exemple, si vous souhaitez la conserver, rien n'est fait ici.
        // Sinon, pour la mettre à null, vous pouvez ajouter : else { demande.setMissionDiplomatique(null); }

        // Mise à jour d'autres informations depuis le DTO
        demande.setDateValidation(LocalDate.now());
        demande.setMotifRejet(demandeDtoDto.getMotifRejet());
        demande.setStatus(EStatus.REJETER);

        // Si d'autres champs doivent être mis à jour, faites-le de manière sélective
        // pour éviter de remplacer des objets persistés par des instances transitoires.

        BaDemande updatedDemande = baDemandeRepository.save(demande);

        // Envoi d'un email de notification
        String motifR = updatedDemande.getMotifRejet();
        mailService.sendMessage(
                updatedDemande.getUser().getEmail(),
                updatedDemande.getUser().getNom() + " " + updatedDemande.getUser().getPrenom(),
                "Désolé, votre demande vient d'être rejetée pour le motif :\n" + motifR,
                "Demande d'immatriculation"
        );
        return mapper.maps(updatedDemande);
    }



    /**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto rejeterDemandeParDg(final String id, final BaDemandeDto demandeDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande par le DGPE " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.REJETER_DG);
        demande.setDateValidationDg(LocalDate.now());
        demande.setMotifRejet(demandeDto.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        String motifRj=updatedDemande.getMotifRejet();
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Desolé votre demande vient d'être rejeté pour motif :\n "+motifRj,"Demande de carte");
        return mapper.maps(updatedDemande);


    }
    /**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
    @Override
    public BaDemandeDto rejeterDemandeParDG(final String id, final BaDemandeDto demandeDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande par le DGPE " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.REJETER_DG);
        demande.setDateValidationDg(LocalDate.now());
        demande.setMotifRejet(demandeDto.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
        String motifRj=updatedDemande.getMotifRejet();
        mailService.sendMessage(demande.getUser().getEmail(), demande.getUser().getNom() + " " + demande.getUser().getPrenom(),
                "Desolé votre demande vient d'être rejeté pour motif :\n "+motifRj,"Demande de carte");
        return mapper.maps(updatedDemande);


    }
 /**
     * Fonction de rejet une demande par le service technique.
     * @param id: id de la demande
     * @return BaDemandeDto
     */
 @Override
 public BaDemandeDto rejeterParDG(final String id, final BaDemandeDto demandeDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de la demande par le DGPE " + id));

        BaDemande demande= baDemandeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette demande n'existe pas"));

        demande.setStatus(EStatus.ATTENTE_REJET_DG);
        demande.setDateValidationDg(LocalDate.now());
        demande.setMotifRejet(demandeDto.getMotifRejet());  // Vous pouvez modifier ou passer le motif depuis une méthode
        BaDemande updatedDemande = baDemandeRepository.save(demande);
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
       // String libelle = String.format("%s_%s", documentDto.getTypeDocument().name(), documentDto.getNumDocument());
        // Récupérer la demande associée à partir de documentDto (si applicable)
        if (documentDto.getIdDemande() != null) {
            BaDemande demande = baDemandeRepository.findById(documentDto.getIdDemande())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "la demande est introuvable avec l'ID fourni."));
            baDocument.setDemande(demande);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ID de la demande est obligatoire pour enregistrer un document.");
        }
        baDocument.setId(BaUtils.randomUUID());
        baDocument.setLibelle(documentDto.getTypeDocument()+"");
        baDocument.setUrl(filePath);
        baDocument.setTypeDocument(documentDto.getTypeDocument());
        //baDocument.setDemande(demande);
        return baDocumentRepository.save(baDocument);

    }

    @Override
    @Transactional
    public BaDemandeDto createDemandeWithDocuments(BaDemandeDto demandeDto, List<BaDocumentDto> documentDtos, List<MultipartFile> files) {

        if (documentDtos.size() < 3 || files.size() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Au moins 3 documents sont requis.");
        }

        logService.log(new BaLogDto(EAction.C, "Création de la demande " + demandeDto.getNumeroDemande()));

        BaDemande demande = mapper.maps(demandeDto);

        // Mission diplomatique facultative
        if (demandeDto.getECarte() == ECarte.CARTE_DIPLOMATIQUE && demandeDto.getIdMissionDiplomatique() != null) {
            BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(demandeDto.getIdMissionDiplomatique())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mission diplomatique introuvable"));
            demande.setMissionDiplomatique(mission);
        } else {
            demande.setMissionDiplomatique(null);
        }


        // Utilisateur
        BaUser user = baUserRepository.findById(demandeDto.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utilisateur introuvable"));
        demande.setUser(user);

        // Génération numéro demande
        int annee = LocalDate.now().getYear();
        long sequence = baDemandeRepository.countDemandeByTypeAndYear(demandeDto.getECarte(), annee) + 1;
        String numeroDemande;
        do {
            numeroDemande = BaUtils.generateNumeroDemande(
                    demandeDto.getECarte() == ECarte.CARTE_DIPLOMATIQUE ? "D" : "A", sequence++);
        } while (baDemandeRepository.existsByNumeroDemande(numeroDemande));

        demande.setId(BaUtils.randomUUID());
        demande.setNumeroDemande(numeroDemande);
        demande.setDateDemande(LocalDate.now());
        demande.setStatus(EStatus.ENCOURS);

        BaDemande savedDemande = baDemandeRepository.save(demande);

        // Associer documents
        for (int i = 0; i < documentDtos.size(); i++) {
            BaDocumentDto docDto = documentDtos.get(i);
            MultipartFile file = files.get(i);

            String path = baFileStorageService.saveFile(file);

            BaDocument doc = mapper.maps(docDto);
            doc.setId(BaUtils.randomUUID());
            doc.setUrl(path);
            doc.setLibelle(docDto.getTypeDocument().toString());
            doc.setTypeDocument(docDto.getTypeDocument());
            doc.setDemande(savedDemande);

            baDocumentRepository.save(doc);
        }

        return mapper.maps(savedDemande);
    }



    @Transactional
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
    /***
     * Gestion des cartes (avant la production)
     */

    /**
     * Récupère la liste des cartes en fonction d'un statut donné et du type de carte (via la demande associée).
     *
     * @param statut  le statut des cartes.
     * @param eCarte  le type de carte (CARTE_DIPLOMATIQUE ou CARTE_ACCES).
     * @return la liste des cartes filtrées sous forme de DTO.
     */
    @Override
    public List<BaCarteDto> getCartesByStatutAndType(EStatut statut, ECarte eCarte) {
        List<BaCarte> cartes = baCarteRepository.findByStatutAndDemande_ECarte(statut.name(), eCarte.name());
        return cartes.stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }


    @Override
    public BaCarteDto createCarteEnProduction(String idDemande, Integer moisExpiration) {


        // Récupérer la demande correspondante
        BaDemande demande = baDemandeRepository.findById(idDemande)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Demande introuvable"));

        // Vérifier si une carte est déjà produite pour cette demande et si l'une d'elles est toujours active (non expirée)
        List<BaCarte> cartesExistantes = baCarteRepository.findByDemandeId(idDemande);
        boolean carteActiveExiste = cartesExistantes.stream()
                .anyMatch(c -> c.getDateExpiration() != null && c.getDateExpiration().isAfter(LocalDate.now()));
        if (carteActiveExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Une carte active existe déjà pour cette demande.");
        }

        // Création d'une nouvelle carte
        BaCarte carte = new BaCarte();
        carte.setId(BaUtils.randomUUID());
        carte.setDateProduction(LocalDate.now());
        carte.setDemande(demande);

        // Déterminer la date d'expiration en fonction du type de carte
        if (demande.getECarte() == ECarte.CARTE_DIPLOMATIQUE) {
            // Pour une carte diplomatique, la date d'expiration est la date de production + 3 ans
            carte.setDateExpiration(LocalDate.now().plusYears(3));
        } else if (demande.getECarte() == ECarte.CARTE_ACCES) {
            // Pour une carte d'accès, l'utilisateur doit spécifier le nombre de mois d'expiration
            if (moisExpiration == null || moisExpiration <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Le nombre de mois d'expiration doit être précisé pour une carte d'accès.");
            }
            carte.setDateExpiration(LocalDate.now().plusMonths(moisExpiration));
        } else {
            // Si le type de carte n'est pas reconnu, on peut lever une exception ou gérer autrement
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de carte non supporté.");
        }

        // Générer et affecter les codes (production et code-barres)
        String codeProduction = generateCodeProduction(demande);
        carte.setCodeProduction(codeProduction);

        String codeBarre = generateCodeBarre(demande);
        carte.setCodeBarre(codeBarre);

        // Sauvegarder la nouvelle carte
        BaCarte savedCarte = baCarteRepository.save(carte);
        return mapper.maps(savedCarte);

    }

    /*@Override
    public BaCarteDto createCarteProduction(String idDemande) {
        return null;
    }*/
    @Override
    public List<BaCarteDto> listeCarteProduit(List<BaDemandeDto> demandes) {
        List<BaCarteDto> listeCarte = new ArrayList<>();

        for (BaDemandeDto demandeDto : demandes) {
            String demandeId = demandeDto.getId();

            try {
                // Vérifie s’il existe une carte active pour cette demande
                Optional<BaCarte> carteActive = baCarteRepository.findByDemandeId(demandeId).stream()
                        .filter(c -> c.getDateExpiration() != null && c.getDateExpiration().isAfter(LocalDate.now()))
                        .findFirst();

                if (carteActive.isPresent()) {
                    // Ajouter la carte active existante
                    listeCarte.add(mapper.maps(carteActive.get()));
                    logService.log(new BaLogDto(EAction.V, "Carte active récupérée pour la demande : " + demandeId));
                } else {
                    // Produire une nouvelle carte car aucune active n’existe
                    BaCarteDto nouvelleCarte = createCarteProduction(demandeId, demandeDto);
                    listeCarte.add(nouvelleCarte);
                    logService.log(new BaLogDto(EAction.C, "Nouvelle carte produite pour la demande : " + demandeId));
                }

            } catch (ResponseStatusException e) {
                // Gérer proprement l’erreur pour ne pas bloquer les autres demandes
                logService.log(new BaLogDto(EAction.U, "Erreur pour la demande " + demandeId + " : " + e.getReason()));
            }
        }

        return listeCarte;
    }



    @Override
    public BaCarteDto createCarteProduction(String idDemande, BaDemandeDto demandeDto) {
        BaDemande demande = baDemandeRepository.findById(idDemande)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Demande introuvable."));

        boolean carteActiveExiste = baCarteRepository.findByDemandeId(idDemande).stream()
                .anyMatch(c -> c.getDateExpiration() != null && c.getDateExpiration().isAfter(LocalDate.now()));
        if (carteActiveExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Une carte active existe déjà pour cette demande.");
        }

        BaCarte carte = new BaCarte();
        carte.setId(BaUtils.randomUUID());
        carte.setDateProduction(LocalDate.now());
        carte.setDemande(demande);

        switch (demande.getECarte()) {
            case CARTE_DIPLOMATIQUE -> carte.setDateExpiration(LocalDate.now().plusYears(3));
            case CARTE_ACCES -> carte.setDateExpiration(LocalDate.now().plusYears(1));
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de carte inconnu.");
        }

        carte.setCodeProduction(generateCodeProduction(demande));
        carte.setCodeBarre(generateCodeBarre(demande));

        BaCarte savedCarte = baCarteRepository.save(carte);

        logService.log(new BaLogDto(EAction.C, "Création carte : Demande " + idDemande + ", carte " + carte.getCodeProduction()));

        // Mise à jour du statut de la demande
        // produireDemande(idDemande, demandeDto);

        return mapper.maps(savedCarte);
    }



    @Scheduled(cron = "0 0 0 * * *")
    @Override
        public void desactiverCartesExpirees() {
            log.info("Début de la tâche planifiée pour désactiver les cartes expirées...");

            // Récupérer toutes les cartes actives dont la date d'expiration est passée
            List<BaCarte> cartesExpirees = baCarteRepository.findByDateExpirationBeforeAndStatut(LocalDate.now(), EStatut.A);

            if (cartesExpirees.isEmpty()) {
                log.info("Aucune carte expirée trouvée.");
            } else {
                // Mettre à jour le statut de chaque carte expirée
                cartesExpirees.forEach(carte -> {
                    carte.setStatut(EStatut.D); // "D" pour désactivé
                });
                baCarteRepository.saveAll(cartesExpirees);
                log.info("{} carte(s) expirée(s) désactivée(s).", cartesExpirees.size());
                logService.log(new BaLogDto(EAction.U, cartesExpirees.size() + " cartes expirées ont été désactivées automatiquement."));
            }
        }

    @Override
    public void desactiverUneCarte(String idCarte) {
        // Vérification de l'existence de la carte
        BaCarte carte = baCarteRepository.findById(idCarte)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte introuvable avec l'ID : " + idCarte));

        // Vérification si la carte est déjà désactivée
        if (carte.getStatut() == EStatut.D) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La carte est déjà désactivée.");
        }

        // Mise à jour du statut de la carte
        carte.setStatut(EStatut.D);
        baCarteRepository.save(carte);

        logService.log(new BaLogDto(EAction.U, "Désactivation de la carte ID : " + idCarte));
    }


    private String generateCodeProduction(BaDemande demande) {
        return demande.getNumeroDemande() + "-" +
                demande.getNom().toUpperCase() + "-" +
                demande.getPrenom().toUpperCase() + "-" +
                demande.getDateNaissance();
    }

    private String generateCodeBarre(BaDemande demande) {
        String randomCode = String.format("%08d", new Random().nextInt(100000000)); // Générer un nombre aléatoire de 8 chiffres
        return demande.getNumeroDemande() + "-MAE-" + randomCode;
    }



    /**Gestion du reporting (Statisques)
     */

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

    /**
     * Fonction aui retourne les statistiques globales, ou statistique en foncion de type de carte
     * @param eCarte
     * @return
     */
    @Override
    public BaStatistiqueTotalDto getStatistics(ECarte eCarte) {
        if (eCarte != null) {
            return baDemandeRepository.getStatisticsByCarte(eCarte);
        } else {
            return baDemandeRepository.getGlobalStatistics();
        }
    }

    /**
     * Service permettant de retourner les statistiques des demandes totale par mois et pour l'année actuelle
     * @param eCarte: le typedde carte
     * @return: un objet de demamdeDto
     */

    @Override
    public List<BaStatistiquesDto> getDemandesByCurrentYearAndCarte(ECarte eCarte) {
        int anneeCourante = LocalDate.now().getYear(); // Récupérer l'année actuelle

        // Récupère les résultats agrégés depuis le repository
        List<Object[]> results = baDemandeRepository.countDemandesByMonthAndType(anneeCourante, eCarte);

        // Initialise un tableau des mois (1 à 12) avec zéro par défaut
        Map<Integer, Long> demandesParMois = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            demandesParMois.put(i, 0L); // Initialise chaque mois avec zéro
        }

        // Remplit les mois ayant des valeurs à partir des résultats
        for (Object[] result : results) {
            Integer mois = (Integer) result[0]; // Mois (1 - 12)
            Long count = (Long) result[1];      // Nombre de demandes
            demandesParMois.put(mois, count);
        }

        // Convertit les résultats en une liste de DTO avec libellé du mois
        return demandesParMois.entrySet()
                .stream()
                .map(entry -> new BaStatistiquesDto(
                        BaUtils.getMonthLabel(entry.getKey()), // Convertit le numéro du mois en libellé
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Service permettant de retourner les statistiques des demandes totales par mois
     * et pour une année spécifique.
     *
     * @param annee  L'année pour laquelle les statistiques sont demandées.
     * @param eCarte Le type de carte (CARTE_DIPLOMATIQUE ou CARTE_ACCES).
     * @return Une liste d'objets BaStatistiquesDto contenant les statistiques.
     */
    @Override
    public List<BaStatistiquesDto> getDemandesByYearAndCarte(int annee, ECarte eCarte) {
        // Récupère les résultats agrégés depuis le repository
        List<Object[]> results = baDemandeRepository.countDemandesByMonthAndType(annee, eCarte);

        // Initialise un tableau des mois (1 à 12) avec zéro par défaut
        Map<Integer, Long> demandesParMois = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            demandesParMois.put(i, 0L); // Initialise chaque mois avec zéro
        }

        // Remplit les mois ayant des valeurs à partir des résultats
        for (Object[] result : results) {
            Integer mois = (Integer) result[0]; // Mois (1 - 12)
            Long count = (Long) result[1];      // Nombre de demandes
            demandesParMois.put(mois, count);
        }

        // Convertit les résultats en une liste de DTO avec libellé du mois
        return demandesParMois.entrySet()
                .stream()
                .map(entry -> new BaStatistiquesDto(
                        BaUtils.getMonthLabel(entry.getKey()), // Convertit le numéro du mois en libellé
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public BaStatistiqueCarteDto getCarteStatisticsByYear(int annee) {
        return baDemandeRepository.countCarteByTypeAndYear(annee);
    }

    @Override
    public BaStatistiqueCarteDto getCarteStatisticsForCurrentYear() {
        int currentYear = LocalDate.now().getYear(); // Année actuelle
        return getCarteStatisticsByYear(currentYear);
    }

    /**
     * End point pour télecharer un document
     * @param demandeId:Id de la demande
     * @return
     */


    @Override
    public ResponseEntity<byte[]> lireOuTelechargerPhoto(String demandeId, boolean download) {
        // Étape 1 : Récupérer la photo liée à la demande
        BaDocument photo = baDocumentRepository
                .findByTypeDocumentAndDemandeId(EDocument.PHOTO, demandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo introuvable pour la demande."));

        // Étape 2 : Récupérer la demande pour avoir le nom/prénom
        BaDemande demande = baDemandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Demande introuvable."));

        String nom = demande.getNom() != null ? demande.getNom().trim() : "";
        String prenom = demande.getPrenom() != null ? demande.getPrenom().trim() : "";
        String numeroDemande = demande.getNumeroDemande() != null ? demande.getNumeroDemande().trim() : "";

        // Nettoyage des espaces et caractères spéciaux si besoin
        String nomPrenom = (nom+"-"+prenom+"-"+numeroDemande).replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "_");

        // Étape 3 : Lire le fichier
        byte[] contenu = baFileStorageService.getDocument(photo.getId());

        // Étape 4 : Nom du fichier de téléchargement
        String nomFichier = nomPrenom.isEmpty() ? "photo.jpg" : nomPrenom + ".jpg";

        // Étape 5 : Préparer les en-têtes HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDisposition(
                download
                        ? ContentDisposition.attachment().filename(nomFichier).build()
                        : ContentDisposition.inline().filename(nomFichier).build()
        );

        // Étape 6 : Retourner la réponse
        return new ResponseEntity<>(contenu, headers, HttpStatus.OK);
    }
    @Override
    public void createContacterNous(BaContacterNousDto dto) {

        // Envoi de l'email
        String subject = "Nouveau message reçu de " + dto.getNomPrenom();
        String content = String.format(
                "Nom et prénom : %s\nTéléphone/Email  :%s\nMessage : %s",
                dto.getNomPrenom(),
                dto.getEmailOutelephone(),
                dto.getMessage()
        );

        mailService.sendEmail(
                "abdramanbakouan@gmail.com", // Destinataire
                subject,
                content,
                false, // isMultipart
                false, // isHtml
                "Administrateur"
                // pas de pièces jointes ici
        );
    }




}
