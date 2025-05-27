package com.bakouan.app.service.impl;

import com.bakouan.app.dto.*;
import com.bakouan.app.dto.BaAutorisationSpecialeDto;
import com.bakouan.app.enums.*;
import com.bakouan.app.mapper.YtMapper;
import com.bakouan.app.model.*;
import com.bakouan.app.repositories.*;
import com.bakouan.app.service.*;
import com.bakouan.app.utils.BaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BaAutorisationServiceImpl implements BaAutorisationService {

    @Value("${app.storage.path}")
    private String basePath;

    // Mapper pour convertir entre DTO et entité (utilisation de MapStruct)
    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);

    // Repositories pour l'accès aux données
    private final BaAutorisationRepository autorisationRepository;
    private final BaDocumentAutorisationRepository documentAutorisationRepository;
    private final BaDelegationMembreRepository delegationMembreRepository;
    private final BaDocumentMembreDelegationRepository documentMembreDelegationRepository;
    private final BaLogService logService;
    private final BaMailService mailService;
    private final BaFileStorageService baFileStorageService;
    private final BaAsynchroService asynchroService;
    private final BaUserRepository baUserRepository;
    private final BaMissionDiplomatiqueRepository missionDiplomatiqueRepository;

    /**
     * Méthode utilitaire pour centraliser la récupération d'une autorisation spéciale.
     * Permet d'éviter la duplication du code pour la recherche d'une autorisation par son identifiant.
     *
     * @param id identifiant de l'autorisation spéciale
     * @return l'entité BaAutorisationSpecial correspondante
     * @throws ResponseStatusException si l'autorisation n'est pas trouvée
     */
    private BaAutorisationSpeciale getAutorisationById(String id) {
        return autorisationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable."));
    }

    /**
     * Service permettant d'afficher la liste des autorisations selon leur type(Arrivee, depart, arrivee_depart)
     * @param type : type de la demande
     * @return @ une liste
     */
    @Override
    public List<BaAutorisationSpecialeDto> findByType(ETypeAutorisation type) {
        return autorisationRepository.findByTypeAutorisation(type)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }


    /**
     * Création d'une autorisation spéciale.
     * Ce service enregistre l'autorisation, sauvegarde le document "Note Verbale" associé,
     * et renvoie le DTO correspondant.
     *
     * @param autorisationSpecialeDto DTO contenant les informations de l'autorisation
     * @param noteVerbale             fichier PDF de la note verbale
     * @return DTO de l'autorisation créée
     */
    @Override
    public BaAutorisationSpecialeDto create(final BaAutorisationSpecialeDto autorisationSpecialeDto,
                                            final MultipartFile noteVerbale) {
        // Validation de l'existence de l'utilisateur
        BaUser user = baUserRepository.findById(autorisationSpecialeDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "L'utilisateur est introuvable"));

        // Vérifier qu'au moins une date (d'arrivée ou de départ) est renseignée
        if (autorisationSpecialeDto.getDateDepart() == null && autorisationSpecialeDto.getDateArrivee() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date d'arrivée ou la date de départ (au moins l'une des deux) est requise.");
        }

        // Sauvegarder la note verbale (PDF) et récupérer le chemin d'accès du fichier
        String noteVerbalePath = baFileStorageService.saveFileDocumentPDF(noteVerbale);

        // Conversion du DTO en entité
        BaAutorisationSpeciale autorisation = mapper.maps(autorisationSpecialeDto);

        String currentYear = String.valueOf(Year.now().getValue()).substring(2); // "25"
        List<String> lastNumList = autorisationRepository.findLastNumeroDemandeForYear(currentYear);
        String lastNumero = lastNumList.isEmpty() ? null : lastNumList.get(0);
        String generatedNumero = BaUtils.generateNextNumeroAutorisation(lastNumero, currentYear);

        // Gestion facultative de la mission diplomatique
        if (autorisationSpecialeDto.getIdMissionDiplomatique() != null) {
            BaMissionDiplomatique mission = missionDiplomatiqueRepository.findById(autorisationSpecialeDto.getIdMissionDiplomatique())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "La mission diplomatique est introuvable"));
            autorisation.setMissionDiplomatique(mission);
        } else {
            autorisation.setMissionDiplomatique(null);
        }

        // Initialisation des autres champs

        autorisation.setId(BaUtils.randomUUID());
        autorisation.setNumDemande(generatedNumero);
        autorisation.setUser(user);
        autorisation.setEtat(EEtatAutorisation.NON_SOUMIS);
        autorisation.setDateDemande(LocalDate.now());

        // Enregistrement de l'autorisation dans la base
        BaAutorisationSpeciale savedAutorisation = autorisationRepository.save(autorisation);

        // Création et enregistrement du document associé : la note verbale
        BaDocumentAutorisationSpecial document = new BaDocumentAutorisationSpecial();
        document.setId(BaUtils.randomUUID());
        document.setUrl(noteVerbalePath);
        document.setLibelle("Note Verbale");
        document.setAutorisationSpeciale(savedAutorisation);
        documentAutorisationRepository.save(document);

        // Retour du DTO correspondant à l'autorisation enregistrée
        return mapper.maps(savedAutorisation);
    }

    /**
     * Service permettant de valider une soumission de demande (de NON_SOUMIS EN_ATTENTE)
     * @param id: Identifiant de la demande
     */
    @Override
    public void ValiderDemande(String id) {
        // 1. Mise à jour rapide en base
        int updated = autorisationRepository.updateEtatById(id, EEtatAutorisation.EN_ATTENTE);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable.");
        }

        // 2. Délégation asynchrone du log + email
        asynchroService.apresValideAutorisation(id);
    }



    /**
     * Ajout d'une note verbale à une autorisation spéciale existante.
     * Vérifie que le fichier n'est pas vide et l'associe à l'autorisation.
     *
     * @param autorisationId identifiant de l'autorisation spéciale
     * @param noteVerbale    fichier PDF de la note verbale à ajouter
     * @return DTO de l'autorisation mise à jour
     */
    @Override
    public BaAutorisationSpecialeDto uploadNoteVerbale(final String autorisationId, final MultipartFile noteVerbale) {
        log.info("Ajout d'une note verbale à l'autorisation spéciale ID : {}", autorisationId);

        // Vérifier que le fichier n'est pas vide
        if (noteVerbale == null || noteVerbale.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le fichier de la note verbale est obligatoire.");
        }

        // Récupération de l'autorisation via la méthode utilitaire
        BaAutorisationSpeciale autorisation = getAutorisationById(autorisationId);

        try {
            // Sauvegarde du fichier et création d'un nouveau document associé
            String noteVerbalePath = baFileStorageService.saveFileDocumentPDF(noteVerbale);
            BaDocumentAutorisationSpecial document = new BaDocumentAutorisationSpecial();
            document.setUrl(noteVerbalePath);
            document.setLibelle("Note Verbale");
            document.setAutorisationSpeciale(autorisation);
            documentAutorisationRepository.save(document);

            log.info("Note verbale ajoutée avec succès à l'autorisation spéciale ID : {}", autorisationId);
            return mapper.maps(autorisation);
        } catch (Exception e) {
            // Gestion des erreurs : en cas d'exception, on est log l'erreur et on renvoie une réponse d'erreur
            log.error("Erreur lors de l'ajout de la note verbale : {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue lors de l'ajout de la note verbale.");
        }
    }

    /**
     * Suppression d'une note verbale associée à une autorisation spéciale.
     * Vérifie que le document appartient bien à l'autorisation et le supprime.
     *
     * @param autorisationId identifiant de l'autorisation spéciale
     * @param documentId     identifiant du document de la note verbale
     * @return DTO de l'autorisation après suppression du document
     */
    @Override
    @Transactional
    public BaAutorisationSpecialeDto removeNoteVerbale(String autorisationId, String documentId) {
        // Recherche du parent et de l’enfant
        BaAutorisationSpeciale autorisation = autorisationRepository.findById(autorisationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable."));
        BaDocumentAutorisationSpecial document = documentAutorisationRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note verbale introuvable."));

        // Vérification de la correspondance
        if (!document.getAutorisationSpeciale().getId().equals(autorisationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La note verbale ne correspond pas à l'autorisation spéciale spécifiée.");
        }

        // **Log de la suppression du fichier physique**
        log.info("Suppression du fichier physique associé à la note verbale '{}' : URL = {}", documentId, document.getUrl());
        baFileStorageService.deleteFile(document.getUrl());

        // Log de la suppression du document en base
        log.info("Suppression du document '{}' pour l'autorisation spéciale '{}'", documentId, autorisationId);

        // Retrait de l’enfant de la collection du parent (orphanRemoval supprimera l’enregistrement DB)
        autorisation.getDocuments().remove(document);

        // Sauvegarde du parent
        BaAutorisationSpeciale updated = autorisationRepository.save(autorisation);

        return mapper.maps(updated);
    }

    /**
     * Mise à jour d'une autorisation spéciale.
     * Permet notamment de mettre à jour les dates d'arrivée et de départ ainsi que d'associer une mission diplomatique.
     *
     * @param id                      identifiant de l'autorisation à mettre à jour
     * @param autorisationSpecialeDto DTO contenant les nouvelles informations
     * @return DTO de l'autorisation mise à jour
     */
    @Override
    public BaAutorisationSpecialeDto update(String id, final BaAutorisationSpecialeDto autorisationSpecialeDto) {
        logService.log(new BaLogDto(EAction.U, "Mise à jour de l'autorisation spéciale avec ID : " + id));
        // Récupération de l'autorisation existante
        BaAutorisationSpeciale existingAutorisation = getAutorisationById(id);

        // Mise à jour de la mission diplomatique si l'identifiant est présent dans le DTO
        if (autorisationSpecialeDto.getIdMissionDiplomatique() != null) {
            BaMissionDiplomatique missionDiplomatique = missionDiplomatiqueRepository.findById(autorisationSpecialeDto.getIdMissionDiplomatique())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mission diplomatique introuvable"));
            existingAutorisation.setMissionDiplomatique(missionDiplomatique);
        }

        // Mise à jour des dates si elles sont fournies dans le DTO
        if (autorisationSpecialeDto.getDateArrivee() != null) {
            existingAutorisation.setDateArrivee(autorisationSpecialeDto.getDateArrivee());
        }
        if (autorisationSpecialeDto.getDateDepart() != null) {
            existingAutorisation.setDateDepart(autorisationSpecialeDto.getDateDepart());
        }

        // Sauvegarde de l'autorisation mise à jour et conversion en DTO
        BaAutorisationSpeciale updatedAutorisation = autorisationRepository.save(existingAutorisation);
        return mapper.maps(updatedAutorisation);
    }

    /**
     * Suppression (ou archivage) d'une autorisation spéciale.
     * Au lieu de supprimer physiquement l'entité, on modifie son statut.
     *
     * @param id identifiant de l'autorisation à supprimer
     */
    @Override
    public void delete(String id) {
        logService.log(new BaLogDto(EAction.D, "Suppression de l'autorisation spéciale ID : " + id));
        BaAutorisationSpeciale autorisation = getAutorisationById(id);
        // Modification du statut pour marquer l'autorisation comme supprimée (archivage)
        autorisation.setStatut(EStatut.D);
        autorisationRepository.save(autorisation);
    }

    /**
     * Recherche d'une autorisation spéciale par son identifiant.
     *
     * @param id identifiant de l'autorisation à rechercher
     * @return DTO de l'autorisation trouvée
     */
    @Override
    public BaAutorisationSpecialeDto findById(String id) {
        log.info("Recherche de l'autorisation spéciale ID : {}", id);
        return mapper.maps(getAutorisationById(id));
    }

    /**
     * Validation d'une autorisation spéciale par le Service Technique.
     *
     * @param id identifiant de l'autorisation à valider
     * @return DTO de l'autorisation validée
     */
    @Override
    public BaAutorisationSpecialeDto validateSt(String id) {
        int updated = autorisationRepository.updateEtatById(id, EEtatAutorisation.VALIDE);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation introuvable pour l'ID : " + id);
        }

        asynchroService.afterValidate(id);

        return BaAutorisationSpecialeDto.builder()
                .id(id)
                .etat(EEtatAutorisation.VALIDE)
                .build();

    }


    /**
     * Rejet d'une autorisation spéciale par le Service Technique.
     *
     * @param id identifiant de l'autorisation à rejeter
     * @return DTO de l'autorisation rejetée
     */
    @Override
    public BaAutorisationSpecialeDto rejectSt(String id, BaAutorisationSpecialeDto autDto) {
        logService.log(new BaLogDto(EAction.U, "Rejet de l'autorisation spéciale par le Service technique ID : " + id));

        BaAutorisationSpeciale autorisation = getAutorisationById(id);

        autorisation.setEtat(EEtatAutorisation.REJETE);
        autorisation.setMotifRejet(autDto.getMotifRejet());

        BaAutorisationSpeciale updated = autorisationRepository.save(autorisation);

        String motifRj = updated.getMotifRejet();
        String fullName = autorisation.getUser().getNom() + " " + autorisation.getUser().getPrenom();

        mailService.sendMessage(
                autorisation.getUser().getEmail(),
                "A " + fullName,
                "Désolé, votre demande vient d'être rejetée pour le motif suivant :\n" + motifRj,
                "Demande d'autorisation spéciale"
        );

        return mapper.maps(updated);
    }


    /**
     * Validation d'une autorisation spéciale par le DG.
     *
     * @param id identifiant de l'autorisation à valider par le DG
     * @return DTO de l'autorisation validée par le DG
     */
    @Override
    public BaAutorisationSpecialeDto validateDg(String id) {
        logService.log(new BaLogDto(EAction.U, "Validation de l'autorisation spéciale par le DG ID : " + id));
        BaAutorisationSpeciale autorisation = getAutorisationById(id);
        autorisation.setEtat(EEtatAutorisation.VALIDE_DG);
        return mapper.maps(autorisationRepository.save(autorisation));
    }

    /**
     * Rejet d'une autorisation spéciale par le DG.
     *
     * @param id identifiant de l'autorisation à rejeter par le DG
     * @return DTO de l'autorisation rejetée par le DG
     */
    @Override
    public BaAutorisationSpecialeDto rejectDg(String id) {
        logService.log(new BaLogDto(EAction.U, "Rejet de l'autorisation spéciale par le DG ID : " + id));
        BaAutorisationSpeciale autorisation = getAutorisationById(id);
        autorisation.setEtat(EEtatAutorisation.REJETER_DG);
        return mapper.maps(autorisationRepository.save(autorisation));
    }

    /**
     * Récupération de toutes les autorisations spéciales actives.
     *
     * @return liste des DTO des autorisations actives
     */
    @Override
    public List<BaAutorisationSpecialeDto> findAll() {
        return autorisationRepository.findByStatut(EStatut.A)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupération de toutes les autorisations spéciales archivées.
     *
     * @return liste des DTO des autorisations archivées
     */
    @Override
    public List<BaAutorisationSpecialeDto> autorisationSpecialArchiv() {
        return autorisationRepository.findByStatut(EStatut.D)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupération de toutes les autorisations spéciales d'un utilisateur donné.
     *
     * @param userId identifiant de l'utilisateur
     * @return liste des DTO des autorisations de l'utilisateur
     */
    @Override
    public List<BaAutorisationSpecialeDto> findAllByUser(String userId) {
        return autorisationRepository.findByUserId(userId)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupération des autorisations spéciales selon un état donné.
     *
     * @param etatAutorisation état à filtrer
     * @return liste des DTO des autorisations correspondant à l'état fourni
     */
    @Override
    public List<BaAutorisationSpecialeDto> findValiderParEtat(EEtatAutorisation etatAutorisation) {
        return autorisationRepository.findByEtat(etatAutorisation)
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau membre de délégation.
     *
     * @param dto Le DTO contenant les informations du membre (nom, prénom, fonction, etc.)
     * @return Le DTO du membre créé
     */
    @Override
    public BaDelegationMembreDto createMember(BaDelegationMembreDto dto) {
        // Validation de l'autorisation
        BaAutorisationSpeciale autorisation = autorisationRepository.findById(dto.getIdAutorisationSpeciale())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable"));

        // Mapping et initialisation
        BaDelegationMembre membre = mapper.maps(dto);
        membre.setId(BaUtils.randomUUID());
        membre.setAutorisationSpeciale(autorisation);

        // Sauvegarde
        BaDelegationMembre savedMembre = delegationMembreRepository.save(membre);

        return mapper.maps(savedMembre);
    }

    @Override
    public BaDelegationMembreDto createMember(BaDelegationMembreDto dto,
                                              List<BaDocumentPersonnelAutorisationSpecialDto> docDtoList,
                                              List<MultipartFile> files) {

        // Vérification de l'autorisation
        BaAutorisationSpeciale autorisation = autorisationRepository.findById(dto.getIdAutorisationSpeciale())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable"));

        // Mapping DTO -> Entité
        BaDelegationMembre membre = mapper.maps(dto);
        membre.setId(BaUtils.randomUUID());
        membre.setAutorisationSpeciale(autorisation);

        // Sauvegarde du membre
        BaDelegationMembre savedMembre = delegationMembreRepository.save(membre);

        // Association des documents
        if (docDtoList != null && files != null) {
            if (docDtoList.size() != files.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nombre de fichiers ne correspond pas au nombre de métadonnées.");
            }

            for (int i = 0; i < docDtoList.size(); i++) {
                BaDocumentPersonnelAutorisationSpecialDto docDto = docDtoList.get(i);
                MultipartFile file = files.get(i);
                System.out.println("type ds carte"+docDto.getTypeDocument());
                if (file != null && !file.isEmpty()) {
                    String path = baFileStorageService.saveFileDocumentPDF(file);

                    BaDocumentPersonnelAutorisationSpecial document = new BaDocumentPersonnelAutorisationSpecial();
                    document.setId(BaUtils.randomUUID());
                    document.setLibelle(docDto.getLibelle());
                    document.setTypeDocument(docDto.getTypeDocument());
                    document.setUrl(path);
                    document.setMembre(savedMembre);
                    documentMembreDelegationRepository.save(document);
                }
            }
        }

        return mapper.maps(savedMembre);
    }

    @Override
    public void deleteMember(String membreId) {
        // Récupération du membre
        BaDelegationMembre membre = delegationMembreRepository.findById(membreId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Membre non trouvé"));

        // Suppression explicite des documents liés au membre (si pas en cascade)
        List<BaDocumentPersonnelAutorisationSpecial> documents =
                documentMembreDelegationRepository.findByMembreId(membreId);

        documentMembreDelegationRepository.deleteAll(documents);

        // Suppression du membre
        delegationMembreRepository.delete(membre);

        // Log de l'action
        logService.log(new BaLogDto(EAction.D, "Suppression du membre de délégation : " + membre.getNom()));
    }




    /**
     * Ajoute un document à un membre existant.
     *
     * @param membreId L'identifiant du membre auquel ajoute le document
     * @param file     Le fichier a ajouté (par exemple, un document PDF).
     * @return Le DTO du membre mis à jour incluant le nouveau document
     */
    @Override
    public BaDelegationMembreDto addDocumentToMember(String membreId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le fichier est obligatoire.");
        }

        // Récupération du membre
        BaDelegationMembre membre = delegationMembreRepository.findById(membreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre non trouvé."));

        try {
            // Sauvegarde du fichier
            String filePath = baFileStorageService.saveFileDocumentPDF(file);

            // Extraction du libellé sans l'extension
            String originalName = file.getOriginalFilename();
            String libelleSansExtension = originalName != null ? originalName.replaceFirst("[.][^.]+$", "") : "DOCUMENT";

            // Tentative de conversion en enum
             EDocumentAutorisation typeDocument;
            try {
                typeDocument = EDocumentAutorisation.valueOf(libelleSansExtension.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de document non reconnu : " + libelleSansExtension);
            }

            // Création et association du document
            BaDocumentPersonnelAutorisationSpecial document = new BaDocumentPersonnelAutorisationSpecial();
            document.setId(BaUtils.randomUUID());
            document.setLibelle(libelleSansExtension);
            document.setUrl(filePath);
            document.setTypeDocument(typeDocument);
            document.setMembre(membre);

            // Sauvegarde via la relation cascade
            membre.getDocuments().add(document);
            BaDelegationMembre updatedMembre = delegationMembreRepository.save(membre);

            return mapper.maps(updatedMembre);
        } catch (Exception e) {
            log.error("Erreur lors de l'ajout du document pour le membre {}: {}", membreId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de l'ajout du document.");
        }
    }


    /**
     * Supprime un document associé à un membre.
     *
     * @param membreId   L'identifiant du membre dont le document sera supprimé
     * @param documentId L'identifiant du document à supprimer
     * @return Le DTO du membre mis à jour après suppression du document
     */
    @Override
    public BaDelegationMembreDto removeDocumentFromMember(String membreId, String documentId) {
        // Récupération du membre dans la base de données
        BaDelegationMembre membre = delegationMembreRepository.findById(membreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre non trouvé."));

        // Recherche du document dans la collection du membre
        Optional<BaDocumentPersonnelAutorisationSpecial> optDocument = membre.getDocuments()
                .stream()
                .filter(doc -> doc.getId().equals(documentId))
                .findFirst();

        if (optDocument.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvé pour ce membre.");
        }

        BaDocumentPersonnelAutorisationSpecial document = optDocument.get();

        try {
            // Retrait du document de la collection du membre
            membre.getDocuments().remove(document);
            // Suppression du document dans la base de données via le repository
            documentMembreDelegationRepository.delete(document);
            // Sauvegarde du membre mis à jour
            BaDelegationMembre updatedMembre = delegationMembreRepository.save(membre);
            // Retour du DTO du membre mis à jour
            return mapper.maps(updatedMembre);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du document {} pour le membre {}: {}", documentId, membreId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression du document.");
        }
    }

    @Override
    public BaDelegationMembreDto getMemberById(String membreId) {
        // Recherche du membre dans la base de données en utilisant l'ID fourni.
        // Si aucun membre n'est trouvé, une ResponseStatusException avec le code 404 (Not Found) est lancée.
        BaDelegationMembre membre = delegationMembreRepository.findById(membreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Membre non trouvé."));

        // Conversion de l'entité récupérée en DTO à l'aide du mapper (par exemple MapStruct)
        return mapper.maps(membre);
    }

    @Override
    public List<BaDelegationMembreDto> getAllMembers() {
        // Récupère tous les membres depuis la base de données.
        List<BaDelegationMembre> members = delegationMembreRepository.findAll();

        // Convertit chaque entité en DTO et collecte les résultats dans une liste.
        return members.stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupère la liste des membres associés à une autorisation spéciale donnée.
     *
     * @param autorisationSpecialeId l'ID de l'autorisation spéciale
     * @return la liste des membres sous forme de DTO
     */
    @Override
    public List<BaDelegationMembreDto> getMembersByAutorisationSpeciale(String autorisationSpecialeId) {
        // Récupération de la liste des membres depuis le repository en filtrant sur l'ID de l'autorisation spéciale.
        List<BaDelegationMembre> membres = delegationMembreRepository.findByAutorisationSpecialeId(autorisationSpecialeId);

        // Si aucun membre n'est trouvé, on peut lever une exception ou retourner une liste vide
        if (membres == null || membres.isEmpty()) {
            log.warn("Aucun membre trouvé pour l'autorisation spéciale ID: {}", autorisationSpecialeId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aucun membre trouvé pour cette autorisation spéciale.");
        }

        // Conversion de la liste d'entités en liste de DTO à l'aide du mapper
        return membres.stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Fonction d'upload de service du document final
     * @param autorisationId: id de l'autorisation spéciale
     * @param fichierFinal: le fichier final
     * @return un dto
     */
    @Override
    public BaDocumentAutorisationSpecialDto uploadDocumentFinal(String autorisationId, MultipartFile fichierFinal) {
        // 1. Vérification de l'autorisation
        BaAutorisationSpeciale autorisation = autorisationRepository.findById(autorisationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Autorisation non trouvée avec l'ID : " + autorisationId));

        if (autorisation.getEtat() != EEtatAutorisation.VALIDE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "L'autorisation doit être validée avant d'ajouter un document final.");
        }

        // 2. Sauvegarde du fichier dans le stockage
        String cheminFichier = baFileStorageService.saveFileDocumentPDF(fichierFinal);
        Path path = Paths.get(basePath, cheminFichier);
        File fichierPdf = path.toFile();

        if (!fichierPdf.exists()) {
            throw new RuntimeException("Le fichier PDF n'existe pas pour l'envoi par e-mail.");
        }

        // 3. Création et sauvegarde du document en base
        BaDocumentAutorisationSpecial documentFinal = new BaDocumentAutorisationSpecial();
        documentFinal.setId(BaUtils.randomUUID());
        documentFinal.setLibelle("Document Final");
        documentFinal.setUrl(cheminFichier);
        documentFinal.setAutorisationSpeciale(autorisation);
        documentAutorisationRepository.save(documentFinal);

        // 4. Mise à jour de l'autorisation avec le document final
        autorisation.setDocumentFinal(documentFinal);
        autorisationRepository.save(autorisation);

        // 5. Préparation et envoi de l’e-mail avec le document final
        try {
            String destinataire = autorisation.getUser().getEmail();
            String nomDestinataire = autorisation.getUser().getPrenom() + " " + autorisation.getUser().getNom();
            String sujet = "Document final de votre autorisation spéciale";

            StringBuilder messageHtml = new StringBuilder();
            messageHtml.append("<p>Bonjour <b>").append(nomDestinataire).append("</b>,</p>")
                    .append("<p>Veuillez trouver en pièce jointe le document final relatif à votre autorisation spéciale validée.</p>")
                    .append("<p>Cordialement,<br><i>Direction Générale du Protocole d'État</i></p>");

            mailService.sendEmail(
                    destinataire,
                    sujet,
                    messageHtml.toString(),
                    true,
                    true,
                    nomDestinataire,
                    fichierPdf
            );

        } catch (Exception e) {
            log.warn("Échec de l'envoi du document final par email : {}", e.getMessage(), e);
        }

        // 6. Retour du DTO
        return mapper.maps(documentFinal);
    }


    /**
     * Service pour supprimer un document final
     * @param autorisationId: identifiant de l'autorisation
     */
    @Override
    public void  deleteDocumentFinal(String autorisationId) {
        BaAutorisationSpeciale autorisation = autorisationRepository.findById(autorisationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Autorisation spéciale non trouvée avec l'ID : " + autorisationId));

        BaDocumentAutorisationSpecial documentFinal = autorisation.getDocumentFinal();

        if (documentFinal == null) {
            throw new IllegalStateException("Aucun document final associé à cette autorisation.");
        }

        // Supprimer physiquement le fichier du système de fichiers si nécessaire
        baFileStorageService.deleteFile(documentFinal.getUrl());

        // Supprimer l'entité du document en base
        documentAutorisationRepository.delete(documentFinal);

        // Dissocier le document de l'autorisation
        autorisation.setDocumentFinal(null);
        autorisationRepository.save(autorisation);
    }



}

