package com.bakouan.app.service.impl;

import com.bakouan.app.dto.*;
import com.bakouan.app.dto.BaAutorisationSpecialeDto;
import com.bakouan.app.enums.EAction;
import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.EStatut;
import com.bakouan.app.mapper.YtMapper;
import com.bakouan.app.model.*;
import com.bakouan.app.repositories.*;
import com.bakouan.app.service.BaAutorisationService;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.service.BaLogService;
import com.bakouan.app.service.BaMailService;
import com.bakouan.app.utils.BaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BaAutorisationServiceImpl implements BaAutorisationService {

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
    private final BaUserRepository baUserRepository;
    private final BaMissionDiplomatiqueRepository missionDiplomatiqueRepository;

    /**
     * Méthode utilitaire pour centraliser la récupération d'une autorisation spéciale.
     * Permet d'éviter la duplication du code pour la recherche d'une autorisation par son identifiant.
     *
     * @param id identifiant de l'autorisation spéciale
     * @return l'entité BaAutorisationSpeciale correspondante
     * @throws ResponseStatusException si l'autorisation n'est pas trouvée
     */
    private BaAutorisationSpeciale getAutorisationById(String id) {
        return autorisationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorisation spéciale introuvable."));
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
    public BaAutorisationSpecialeDto create(final BaAutorisationSpecialeDto autorisationSpecialeDto, final MultipartFile noteVerbale) {

        if (autorisationSpecialeDto.getDateDepart() == null && autorisationSpecialeDto.getDateArrivee() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date d'arrivée ou la date de départ (au moins l'une des deux) est requise.");
        }
        // Sauvegarde de la note verbale et récupération du chemin d'accès du fichier
        String noteVerbalePath = baFileStorageService.saveFileDocumentPDF(noteVerbale);

        // Conversion du DTO en entité et initialisation des champs de base
        BaAutorisationSpeciale autorisation = mapper.maps(autorisationSpecialeDto);
        autorisation.setId(BaUtils.randomUUID());
        autorisation.setEtat(EEtatAutorisation.EN_ATTENTE);

        // Enregistrement de l'autorisation dans la base de données
        BaAutorisationSpeciale savedAutorisation = autorisationRepository.save(autorisation);

        // Création et association d'un document pour la note verbale
        BaDocumentAutorisationSpecial document = new BaDocumentAutorisationSpecial();
        document.setUrl(noteVerbalePath);
        document.setLibelle("Note Verbale");
        document.setAutorisationSpeciale(savedAutorisation);
        documentAutorisationRepository.save(document);

        // Retour du DTO converti depuis l'entité sauvegardée
        return mapper.maps(savedAutorisation);
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
            // Gestion des erreurs : en cas d'exception, on log l'erreur et on renvoie une réponse d'erreur
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
    public BaAutorisationSpecialeDto removeNoteVerbale(final String autorisationId, final String documentId) {
        log.info("Suppression de la note verbale ID : {} pour l'autorisation spéciale ID : {}", documentId, autorisationId);

        // Recherche du document par son identifiant
        BaDocumentAutorisationSpecial document = documentAutorisationRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note verbale introuvable."));

        // Vérification que le document appartient bien à l'autorisation indiquée
        if (!document.getAutorisationSpeciale().getId().equals(autorisationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La note verbale ne correspond pas à l'autorisation spéciale spécifiée.");
        }

        try {
            // Suppression du document de la base de données
            documentAutorisationRepository.delete(document);
            log.info("Note verbale supprimée avec succès pour l'autorisation spéciale ID : {}", autorisationId);
            return mapper.maps(document.getAutorisationSpeciale());
        } catch (Exception e) {
            // Gestion des erreurs
            log.error("Erreur lors de la suppression de la note verbale : {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue lors de la suppression de la note verbale.");
        }
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
        logService.log(new BaLogDto(EAction.U, "Validation de l'autorisation spéciale par le Service technique ID : " + id));
        BaAutorisationSpeciale autorisation = getAutorisationById(id);
        autorisation.setEtat(EEtatAutorisation.VALIDE);
        return mapper.maps(autorisationRepository.save(autorisation));
    }

    /**
     * Rejet d'une autorisation spéciale par le Service Technique.
     *
     * @param id identifiant de l'autorisation à rejeter
     * @return DTO de l'autorisation rejetée
     */
    @Override
    public BaAutorisationSpecialeDto rejectSt(String id) {
        logService.log(new BaLogDto(EAction.U, "Rejet de l'autorisation spéciale par le Service technique ID : " + id));
        BaAutorisationSpeciale autorisation = getAutorisationById(id);
        autorisation.setEtat(EEtatAutorisation.REJETE);
        return mapper.maps(autorisationRepository.save(autorisation));
    }

    /**
     * Validation d'une autorisation spéciale par le DG.
     *
     * @param id identifiant de l'autorisation à valider par le DG
     * @return DTO de l'autorisation validée par le DG
     */
    @Override
    public BaAutorisationSpecialeDto validateDg(String id) {
        logService.log(new BaLogDto(EAction.U, "Validation de l'autorisation spéciale par le DGID ID : " + id));
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
     * GESTION DES MEMBRE DE LA DELEGATION SPECIALE
     */

    /**
     * Crée un nouveau membre de délégation.
     *
     * @param dto Le DTO contenant les informations du membre (nom, prénom, fonction, etc.)
     * @return Le DTO du membre créé
     */
    @Override
    public BaDelegationMembreDto createMember(BaDelegationMembreDto dto) {
        // Conversion du DTO en entité
        BaDelegationMembre membre = mapper.maps(dto);
        // Génération d'un identifiant unique pour le membre
        membre.setId(BaUtils.randomUUID());
        // Sauvegarde du membre dans la base de données
        BaDelegationMembre savedMembre = delegationMembreRepository.save(membre);
        // Conversion de l'entité sauvegardée en DTO et retour
        return mapper.maps(savedMembre);
    }

    /**
     * Ajoute un document à un membre existant.
     *
     * @param membreId L'identifiant du membre auquel ajoute le document
     * @param file     Le fichier a ajouté (par exemple, un document PDF)
     * @return Le DTO du membre mis à jour incluant le nouveau document
     */
    @Override
    public BaDelegationMembreDto addDocumentToMember(String membreId, MultipartFile file) {
        // Vérification que le fichier n'est pas vide
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le fichier est obligatoire.");
        }

        // Récupération du membre par son identifiant
        BaDelegationMembre membre = delegationMembreRepository.findById(membreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre non trouvé."));

        try {
            // Sauvegarde du fichier et récupération du chemin de stockage
            String filePath = baFileStorageService.saveFileDocumentPDF(file);

            // Création d'une nouvelle entité document pour le membre
            BaDocumentPersonnelAutorisationSpecial document = new BaDocumentPersonnelAutorisationSpecial();
            document.setId(BaUtils.randomUUID());
            // On peut définir le libellé par exemple avec le nom original du fichier
            document.setLibelle(file.getOriginalFilename());
            document.setUrl(filePath);
            // Association du document au membre
            document.setMembre(membre);
            // Ajout du document à la collection des documents du membre
            membre.getDocuments().add(document);

            // Sauvegarde du membre mis à jour (cascade : le document sera également sauvegardé)
            BaDelegationMembre updatedMembre = delegationMembreRepository.save(membre);
            // Retour du DTO du membre mis à jour
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

}

