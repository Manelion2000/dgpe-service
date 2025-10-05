package accord.gov.app.service.impl;


import accord.gov.app.dto.*;
import accord.gov.app.dto.BaFichierDto;
import accord.gov.app.enums.*;
import accord.gov.app.mapper.YtMapper;
import accord.gov.app.model.*;
import accord.gov.app.repositories.*;
import accord.gov.app.repositories.specification.BaDocumentSpecification;
import accord.gov.app.service.BaFileStorageService;
import accord.gov.app.service.BaLogService;
import accord.gov.app.service.BaParamService;
import accord.gov.app.utils.BaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;;


@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class BaParamServiceImpl implements BaParamService {
    @Value("${app.storage.path}")
    private String basePath;
    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);
    private final BaLogService logService;
    private final BaFileStorageService fileStorageService;
    private final BaTypeAccordRepository typeAccordRepository;
    private final BaTypeDocumentAffilieRepository typeDocumentAffilieRepository;
    private final BaLangueRepository langueRepository;
    private final BaDomaineRepository domaineRepository;
    private final BaPartieRepository partieRepository;
    private final BaDocumentRepository documentRepository;
    private final BaDocumentAffilieRepository documentAffilieRepository;
    private final BaFichierRepository fichierRepository;
    private final BaDocumentSpecification specification;

    /**
     * Crée un nouveau type d'accord (valable aussi pour les traités)
     * @param dto DTO contenant les informations du type d'accord.
     * @return Le type d'accord sous forme de DTO.
     */
    @Override
    public BaTypeAccordDto createTypeAccord(final BaTypeAccordDto dto) {
        BaTypeAccord entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        entity = typeAccordRepository.save(entity);
        logService.log(new BaLogDto(EAction.CREATE, "Création du type d’accord : " + dto.getLibelle()));

        return mapper.maps(entity);
    }

    /**
     * Récupère un type d'accord par son identifiant.
     * @param id Identifiant du type d'accord ou traité
     * @return Type d'accord ou Traité correspondant sous forme de DTO.
     */
    @Override
    public BaTypeAccordDto getAccordById(final String id) {
        BaTypeAccord entity = typeAccordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"TypeAccord introuvable"));

        logService.log(new BaLogDto(
                EAction.UPDATE, "Consultation du type d’accord ID: " + id));

        return mapper.maps(entity);
    }

    /**
     * Récupère la liste de tous les types d’accords
     */
    @Override
    public List<BaTypeAccordDto> getAlTypeAccord() {
        List<BaTypeAccord> accords = typeAccordRepository.findAll();
        logService.log(new BaLogDto(EAction.VIEW, "Consultation de la liste des types d’accords"));
        return accords.stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une langue existante.
     *
     * @param id  Identifiant du type d'accord à mettre à jour.
     * @param dto Nouvelles données du type d'accord.
     * @return La le type d'accord mise à jour sous forme de DTO.
     */
    @Override
    public BaTypeAccordDto updateTypeAccord(final String id, final BaTypeAccordDto dto) {
        BaTypeAccord entity = typeAccordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "TypeAccord introuvable"));

        entity.setLibelle(dto.getLibelle());
        entity = typeAccordRepository.save(entity);

        logService.log(new BaLogDto(EAction.UPDATE, "Mise à jour du type d’accord ID: " + id));

        return mapper.maps(entity);
    }

    /**
     * Suppression d’un type d’accord
     */
    @Override
    public void deleteTypeAccord(final String id) {
        BaTypeAccord type=typeAccordRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"le type d'accord n'existe pas"));
        type.setStatut(EStatut.D);
        typeAccordRepository.save(type);
        logService.log(new BaLogDto(EAction.DELETE, "Suppression du type d’accord ID: " + id));
    }

    // ===================== TYPE DOCUMENT AFFILIE =====================

    /**
     * Crée un nouveau type de document affilié
     * @param dto DTO contenant les informations
     * @return Le type de document sous forme de DTO.
     */
    @Override
    public BaTypeDocumentAffilieDto createTypeDocumentAffilie(final BaTypeDocumentAffilieDto dto) {
       BaTypeDocumentAffilie entity=mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        entity = typeDocumentAffilieRepository.save(entity);

        logService.log(new BaLogDto(EAction.CREATE, "Création : " + dto.getLibelle()));
        return mapper.maps(entity);
    }

    /**
     * Récupère un domaine par son identifiant.
     *
     * @param id Identifiant du domaine.
     * @return Domaine correspondant sous forme de DTO.
     */
    @Override
    public BaTypeDocumentAffilieDto getTypeDocumentAffilieById(final String id) {
        BaTypeDocumentAffilie entity = typeDocumentAffilieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeDocumentAffilie introuvable"));
        logService.log(new BaLogDto(EAction.VIEW, "Consultation ID: " + id));
        return mapper.maps(entity);
    }

    /**
     * Récupère un domaine par son libellé.
     *
     * @param libelle qui est le libellé du type d'accord.
     * @return  correspondant sous forme de DTO.
     */
    @Override
    public BaTypeDocumentAffilieDto getTypeAccordByLibelle(final String libelle) {
        BaTypeDocumentAffilie entity = typeDocumentAffilieRepository.findByLibelle(libelle)
                .orElseThrow(() -> new RuntimeException("TypeDocumentAffilie introuvable"));
        logService.log(new BaLogDto(EAction.VIEW,  "Consultation Libelle: " + libelle));
        return mapper.maps(entity);
    }

    /**
     * Récupère la liste de tous les types de document affilié
     *
     * @return Liste de type sous forme de DTO.
     */

    @Override
    public List<BaTypeDocumentAffilieDto> getAllTypeDocumentAffilie() {
        List<BaTypeDocumentAffilie> docs = typeDocumentAffilieRepository.findAll();
        logService.log(new BaLogDto(EAction.VIEW, "Consultation liste des type de documents affiliés"));
        return docs.stream().map(mapper::maps).toList();
    }

    @Override
    public BaTypeDocumentAffilieDto updateTypeDocumentAffilie(final String id, final BaTypeDocumentAffilieDto dto) {
        BaTypeDocumentAffilie entity = typeDocumentAffilieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"TypeDocumentAffilie introuvable"));
        entity.setLibelle(dto.getLibelle());
        entity = typeDocumentAffilieRepository.save(entity);

        logService.log(new BaLogDto(EAction.UPDATE, "Mise à jour du type de document ayant ID: " + id));
        return mapper.maps(entity);
    }
    /**
     * Supprime un domaine par son identifiant.
     *
     * @param id  Identifiant du type de document affilié
     */
    @Override
    public void deleteTypeDocumentAffilie(final String id) {
        BaTypeDocumentAffilie entity=typeDocumentAffilieRepository.findById(id)
                .orElseThrow(()->new  ResponseStatusException(HttpStatus.BAD_REQUEST,"le type de document introuvable"));
        entity.setStatut(EStatut.D);
        typeDocumentAffilieRepository.save(entity);
        logService.log(new BaLogDto(EAction.DELETE, "Suppression ID: " + id));
    }


        // ===================== LANGUE =====================

        /**
         * Crée une nouvelle langue.
         *
         * @param dto DTO contenant les informations de la langue.
         * @return La langue créée sous forme de DTO.
         */
        @Override
        public BaLangueDto createLangue(final BaLangueDto dto) {
            BaLangue entity = mapper.maps(dto);
            entity.setId(BaUtils.randomUUID());
            entity = langueRepository.save(entity);

            logService.log(new BaLogDto(EAction.CREATE, "Création  d'une nouvelle langue: " + dto.getLibelle()));
            return mapper.maps(entity);
        }

    /**
     * Récupère la liste de tous les types de document affilié
     *
     * @return Liste de type sous forme de DTO.
     */

    @Override
    public List<BaLangueDto> getAllLangue() {
        List<BaLangue> langues = langueRepository.findAll();
        logService.log(new BaLogDto(EAction.VIEW, "Consultation liste des langues"));
        return langues.stream().map(mapper::maps).toList();
    }

    /**
     * Récupère un domaine par son identifiant.
     *
     * @param id Identifiant du domaine.
     * @return Domaine correspondant sous forme de DTO.
     */
    @Override
    public BaLangueDto getLangueById(final String id) {
        BaLangue entity = langueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Langue introuvable avec ID: " + id));

        logService.log(new BaLogDto(EAction.VIEW,  "Consultation ID: " + id));
        return mapper.maps(entity);
    }

        /**
         * Met à jour une langue existante.
         *
         * @param id  Identifiant de la langue à mettre à jour.
         * @param dto Nouvelles données de la langue.
         * @return La langue mise à jour sous forme de DTO.
         */
        @Override
        public BaLangueDto updateTypeAccord(final String id, final BaLangueDto dto) {
            BaLangue entity = langueRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Langue introuvable avec ID: " + id));

            entity.setLibelle(dto.getLibelle());
            entity = langueRepository.save(entity);

            logService.log(new BaLogDto(EAction.UPDATE, "Mise à jour ID: " + id));
            return mapper.maps(entity);
        }

        /**
         * Supprime une langue par son identifiant.
         * @param id  Identifiant de la langue.
         */
        @Override
        public void deleteLangue(final String id) {
            BaLangue entity=langueRepository.findById(id)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"la langue introuvable"));
            entity.setStatut(EStatut.D);
            langueRepository.save(entity);
            logService.log(new BaLogDto(EAction.DELETE,  "Suppression ID: " + id));
        }

        // ===================== DOMAINE =====================

        /**
         * Crée un nouveau domaine.
         *
         * @param dto DTO contenant les informations du domaine.
         * @return Le domaine créé sous forme de DTO.
         */
        @Override
        public BaDomaineDto createDomaine(final BaDomaineDto dto) {
            BaDomaine entity = mapper.maps(dto);
            entity.setId(BaUtils.randomUUID());
            entity = domaineRepository.save(entity);

            logService.log(new BaLogDto(EAction.CREATE, "Création d'un nouveau domaine : " + dto.getLibelle()));
            return mapper.maps(entity);
        }

        /**
         * Récupère un domaine par son identifiant.
         *
         * @param id Identifiant du domaine.
         * @return Domaine correspondant sous forme de DTO.
         */
        @Override
        public BaDomaineDto getDomaineById(final String id) {
            BaDomaine entity = domaineRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Domaine introuvable avec ID: " + id));

            logService.log(new BaLogDto(EAction.VIEW,  "Consultation ID: " + id));
            return mapper.maps(entity);
        }

        /**
         * Récupère la liste de tous les domaines.
         *
         * @return Liste des domaines sous forme de DTO.
         */
        @Override
        public List<BaDomaineDto> getAllDomaine() {
            List<BaDomaine> domaines = domaineRepository.findAll();

            logService.log(new BaLogDto(EAction.VIEW, "Consultation liste de tous les domaines"));
            return domaines.stream().map(mapper::maps).collect(Collectors.toList());
        }

        /**
         * Met à jour un domaine existant.
         *
         * @param id  Identifiant du domaine à mettre à jour.
         * @param dto Nouvelles données du domaine.
         * @return Domaine mis à jour sous forme de DTO.
         */
        @Override
        public BaDomaineDto updateDomaine(final String id, final BaDomaineDto dto) {
            BaDomaine entity = domaineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Domaine introuvable avec ID: " + id));

            entity.setLibelle(dto.getLibelle());
            entity = domaineRepository.save(entity);

            logService.log(new BaLogDto(EAction.UPDATE, "Mise à jour d'un domaine avec ID: " + id));
            return mapper.maps(entity);
        }

        /**
         * Supprime un domaine par son identifiant.
         *
         * @param id  Identifiant du domaine.
         */
        @Override
        public void deleteDomaine(final String id) {
            BaDomaine entity=domaineRepository.findById(id)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"domaine introuvbale"));
            entity.setStatut(EStatut.D);
            domaineRepository.save(entity);
            logService.log(new BaLogDto(EAction.DELETE,  "Suppression  avec libelle: " +id));
        }
    // ===================== PARTIE PRENANTES =====================

    /**
     * Crée un nouveau domaine.
     *
     * @param dto DTO contenant les informations du domaine.
     * @return Le domaine créé sous forme de DTO.
     */
    @Override
    public BaPartieDto createPartie(final BaPartieDto dto) {
        BaPartie entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        entity = partieRepository.save(entity);

        logService.log(new BaLogDto(EAction.CREATE, "Création d'un nouveau partie : " + dto.getLibelle()));
        return mapper.maps(entity);
    }
    /**
     * Récupère une partie par son identifiant unique.
     *
     * @param id Identifiant de la partie
     * @return DTO de la partie trouvée
     * @throws ResponseStatusException si aucune partie n'est trouvée avec cet ID
     */
    @Override
    public BaPartieDto getPartieById(final String id) {
        BaPartie partie = partieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Partie introuvable avec l'id : " + id));

        logService.log(new BaLogDto(EAction.VIEW, "Consultation d'une partie avec ID : " + id));

        return mapper.maps(partie);
    }

    /**
     * Récupère toutes les parties existantes dans la base.
     *
     * @return Liste des DTO de parties
     */
    @Override
    public List<BaPartieDto> getAllPartie() {
        logService.log(new BaLogDto(EAction.VIEW, "Consultation de toutes les parties"));

        return partieRepository.findAll()
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une partie existante.
     *
     * @param id  Identifiant de la partie à mettre à jour
     * @param dto Données modifiées de la partie
     * @return DTO de la partie mise à jour
     * @throws ResponseStatusException si la partie n'existe pas
     */
    @Override
    public BaPartieDto updatePartie(final String id, final BaPartieDto dto) {
        BaPartie partie = partieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Partie introuvable avec l'id : " + id));

        partie.setLibelle(dto.getLibelle());
        partie.setSigle(dto.getSigle());
        partie.setTypePartie(dto.getTypePartie());

        BaPartie updated = partieRepository.save(partie);

        logService.log(new BaLogDto(EAction.UPDATE, " Mise à jour d'une parti avec ID : " + id + ", Nouveau libellé : " + dto.getLibelle()));

        return mapper.maps(updated);
    }

    /**
     * Supprime une partie existante.
     *
     * @param id  Identifiant de la partie à supprimer
     * @param dto DTO contenant des détails contextuels (facultatif pour le log)
     * @throws ResponseStatusException si la partie n'existe pas
     */
    @Override
    public void deletePartie(final String id, final BaPartieDto dto) {
        BaPartie partie = partieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Partie introuvable avec l'id : " + id));
        partie.setStatut(EStatut.D);
        partieRepository.save(partie);

        logService.log(new BaLogDto(EAction.DELETE,  " avec  d'une partieID : " + id + ", Libellé : " + dto.getLibelle()));
    }

    /**
     * Recherche une partie par son libellé.
     *
     * @param libelle Libellé de la partie recherchée
     * @param dto     DTO pour log (facultatif)
     * @return DTO de la partie trouvée
     * @throws ResponseStatusException si aucune partie n'est trouvée avec ce libellé
     */
    @Override
    public BaPartieDto getPartieByLibelle(final String libelle, final BaPartieDto dto) {
        BaPartie partie = partieRepository.findByLibelle(libelle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Partie introuvable avec le libellé : " + libelle));

        logService.log(new BaLogDto(EAction.VIEW,  "Consultation  d'une partie  avec Libellé : " + libelle));

        return mapper.maps(partie);
    }

    /**
     * Crée un nouveau traité ou accord.
     *
     * @param dto DTO contenant les informations l'accord ou traité.
     * @return L'accord ou traité créé sous forme de DTO.
     */
    @Override
    public BaDocumentDto createDocumentP(BaDocumentDto dto, List<MultipartFile> files) {
        // 1. Mapper le DTO vers l’entité
        BaDocument entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        // 2. Sauvegarder le document d'abord (pour générer l’ID et gérer les relations)
        documentRepository.save(entity);

        // 3. Si des fichiers sont fournis
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                // 3.1 Sauvegarder physiquement le fichier (retourne le nom unique)
                String savedFileName = fileStorageService.saveFileDocumentPDF(file);

                // 3.2 Créer une entité BaFichier associée au document
                BaFichier fichier = BaFichier.builder()
                        .id(BaUtils.randomUUID())
                        .libelle(file.getOriginalFilename()) // le vrai nom d’origine
                        .url(savedFileName) // chemin/nom unique du fichier sauvegardé
                        .accord(entity) // relation ManyToOne
                        .affilie(null)
                        .build();

                // 3.3 Ajouter au set de fichiers du document
                entity.getFichiers().add(fichier);
            }
        }

        // 4. Sauvegarder encore pour persister les fichiers liés
        BaDocument saved = documentRepository.save(entity);

        // 5. Logger
        logService.log(new BaLogDto(EAction.CREATE, "Création du document : " + dto.getIntitule()));

        // 6. Retourner le DTO
        return mapper.maps(saved);
    }

    @Override
    public BaDocumentDto createDocument(BaDocumentDto dto, List<MultipartFile> files) {
        // 1. Mapper DTO → entité
        BaDocument entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        documentRepository.save(entity);

        // 2. Traiter les fichiers
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String savedFileName = fileStorageService.saveFileDocumentPDF(file);
                buildAndAttachFile(file, savedFileName, entity); // Accord non null
            }
        }

        // 3. Persister avec ses fichiers
        BaDocument saved = documentRepository.save(entity);

        // 4. Log
        logService.log(new BaLogDto(EAction.CREATE, "Création du document : " + dto.getIntitule()));

        return mapper.maps(saved);
    }
    /**
     * Mise à jour d'un document (supprimer et remplacer un fichier)
     * @param documentId: identifiant de document
     * @param newFiles: nouveau fichier
     * @param filesToDelete: fichier à supprimer
     * @return un Dto de document
     * @throws IOException: une exception à lever
     */
    @Override
    public BaDocumentDto updateDocumentWithFiles(String documentId,
                                                 List<MultipartFile> newFiles,
                                                 List<String> filesToDelete) throws IOException {
        // Charger le document
        BaDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Document introuvable"));

        // 1. Supprimer les fichiers demandés
        if (filesToDelete != null) {
            for (String fileId : filesToDelete) {
                BaFichier fichier = fichierRepository.findById(fileId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Fichier introuvable"));
                document.getFichiers().remove(fichier);
                fichierRepository.delete(fichier);

                // suppression du fichier physique (après succès transaction)
                registerFileDeletion(fichier.getUrl());
            }
        }

        // 2. Ajouter de nouveaux fichiers
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                String savedFileName = fileStorageService.saveFileDocumentPDF(file); // méthode utilitaire
                BaFichier fichier = BaFichier.builder()
                        .id(BaUtils.randomUUID())
                        .libelle(file.getOriginalFilename())
                        .url(savedFileName)
                        .accord(document)
                        .affilie(null)
                        .build();

                document.getFichiers().add(fichier);

                // suppression du fichier physique si rollback
                registerFileRollback(savedFileName);
            }
        }
        return mapper.maps(documentRepository.save(document));
    }

    /**
 * Mise à jour du traité ou accord
 */

@Override
public BaDocumentDto updateDocument(String id, BaDocumentDto dto) {
    BaDocument entity = documentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document introuvable"));

    entity.setIntitule(dto.getIntitule());
    entity.setCote(dto.getCote());
    entity.setMotCle(dto.getMotCle());
    entity.setResume(dto.getResume());
    entity.setCodeBoite(dto.getCodeBoite());
    entity.setEtatAccordVigueur(dto.getEtatAccordVigueur());
    entity.setConfidentialite(dto.getConfidentialite());
    entity.setDateAdoption(dto.getDateAdoption());
    entity.setDateSignature(dto.getDateSignature());
    entity.setDateEntreeVigueur(dto.getDateEntreeVigueur());
    entity.setDateRatification(dto.getDateRatification());
    entity.setLieuSignature(dto.getLieuSignature());
    entity.setNatureDocument(dto.getNatureDocument());
    documentRepository.save(entity);
    logService.log(new BaLogDto(EAction.UPDATE, "Mise à jour du document ID: " + id));
    return mapper.maps(entity);
}
    /**
     * Supprime une partie existante.
     *
     * @param id  Identifiant de la partie à supprimer
     * @throws ResponseStatusException si la partie n'existe pas
     */

    @Override
    public void deleteDocument(String id) {
        BaDocument entity = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document introuvable"));
        entity.setStatut(EStatut.D);
        documentRepository.save(entity);
        logService.log(new BaLogDto(EAction.DELETE, "Suppression du document ID: " + id));
    }

    @Override
    public BaDocumentDto getByDocumentById(String id) {
        BaDocument entity = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document introuvable"));
        logService.log(new BaLogDto(EAction.VIEW, "Consultation du document ID: " + id));
        return mapper.maps(entity);
    }

    /**
     * la liste des documents entre deux dates
     * @param startDate: date de debut
     * @param endDate; date de fin
     * @return une liste de documents
     */

    public List<BaDocumentDto> getDocumentsByDateSignature(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Les deux dates doivent être renseignées.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La date de fin doit être supérieure ou égale à la date de début.");
        }
        List<BaDocument> liste=documentRepository.findByDateSignatureBetween(startDate, endDate);

        return liste
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaDocumentDto> getAllDocument() {
        List<BaDocument> list = documentRepository.findAll();
        logService.log(new BaLogDto(EAction.VIEW, "Consultation de la liste des documents"));
        return list.stream().map(mapper::maps).collect(Collectors.toList());
    }

    @Override
    public List<BaDocumentDto> getAllDocumentsByStatutAndConfidentialite(EStatut statut, EConfidentiel confidentiel) {
        // Récupération des documents
        List<BaDocument> list = documentRepository.findByStatutAndConfidentialite(statut, confidentiel);

        // Construction du message de log selon la confidentialité
        String confidentialiteLabel = (confidentiel == EConfidentiel.OUI) ? "confidentiels" : "non confidentiels";
        logService.log(new BaLogDto(
                EAction.VIEW,
                String.format("Consultation de la liste des documents %s et %s", statut.name(), confidentialiteLabel)
        ));

        // Mapping entités -> DTOs
        return list.stream().map(mapper::maps).collect(Collectors.toList());
    }

    /**
     * Recherche multicritère via Dto Service
     * @param request : request de la Dto
     * @return une liste de document
     */
    @Override
    public List<BaDocumentDto> searchMulticritereViaDto(BaDocumentSearchRequest request) {

        // Vérifier les langues
        if (request.getLangueIds() != null && !request.getLangueIds().isEmpty()) {
            List<String> validLangues = documentRepository.findExistingLangueIds(request.getLangueIds());
            if (validLangues.isEmpty()) return Collections.emptyList();
            request.setLangueIds(validLangues);
        }

        // Vérifier les domaines
        if (request.getDomaines() != null && !request.getDomaines().isEmpty()) {
            List<String> validDomaines = documentRepository.findExistingDomaineIds(request.getDomaines());
            if (validDomaines.isEmpty()) return Collections.emptyList();
            request.setDomaines(validDomaines);
        }

        // Vérifier les parties prenantes
        if (request.getParties() != null && !request.getParties().isEmpty()) {
            List<String> validParties = documentRepository.findExistingPartieIds(request.getParties());
            if (validParties.isEmpty()) return Collections.emptyList();
            request.setParties(validParties);
        }

        // 2️⃣ Construction de la specification
        Specification<BaDocument> spec = Specification.allOf(
                BaDocumentSpecification.byTypeAccord(request.getTypeId()),
                BaDocumentSpecification.byLangues(request.getLangueIds()),
                BaDocumentSpecification.byDomaines(request.getDomaines()),
                BaDocumentSpecification.byParties(request.getParties()),
                BaDocumentSpecification.byMotsCles(request.getKeywords()),
                BaDocumentSpecification.byNature(request.getNature())
        );

        // 3️⃣ Exécution de la recherche
        List<BaDocument> results = documentRepository.findAll(spec);

        // 4️⃣ Logging
        logService.log(new BaLogDto(EAction.VIEW, "Recherche multicritère via specs atomiques"));

        // 5️⃣ Transformation en DTO
        return results.stream().map(mapper::maps).toList();
    }

    public List<BaDocumentDto> searchDocumentsStrict(BaDocumentFilterDto filterDto) {

        Map<String, Object> raw = filterDto.getRawFilters();

        String typeDocumentId = (String) raw.get("typeDocumentId");


// Ensuite dans ton code :
        List<String> langues = safeCastList(raw.get("langues"));
        List<String> domaines = safeCastList(raw.get("domaines"));
        List<String> parties = safeCastList(raw.get("parties"));
        List<String> keywords = safeCastList(raw.get("keywords"));
        ENatureDocument nature = null;
        try {
            if (raw.get("natureDocument") != null)
                nature = ENatureDocument.valueOf(raw.get("natureDocument").toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList(); // champ enum invalide
        }

        // Vérification stricte des IDs
        if (!langues.isEmpty() && langueRepository.countByIdIn(langues) != langues.size()) {
            return Collections.emptyList(); // certains IDs de langue inexistants
        }
        if (!domaines.isEmpty() && domaineRepository.countByIdIn(domaines) != domaines.size()) {
            return Collections.emptyList(); // certains IDs de domaine inexistants
        }
        if (!parties.isEmpty() && partieRepository.countByIdIn(parties) != parties.size()) {
            return Collections.emptyList(); // certains IDs de partie inexistants
        }

        // Création de la specification
        Specification<BaDocument> spec = Specification.allOf(
                BaDocumentSpecification.byTypeAccord(typeDocumentId),
                BaDocumentSpecification.byLangues(langues),
                BaDocumentSpecification.byDomaines(domaines),
                BaDocumentSpecification.byParties(parties),
                BaDocumentSpecification.byMotsCles(keywords),
                BaDocumentSpecification.byNature(nature)
        );

        List<BaDocument> results = documentRepository.findAll(spec);
        return results.stream().map(mapper::maps).toList();
    }
    @SuppressWarnings("unchecked")
    private List<String> safeCastList(Object obj) {
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .filter(Objects::nonNull)
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Recherche ene utilisant le sql natif
     * @param request: le dto de la recherche
     * @return une liste de document Dto
     */

    @Override
    public List<BaDocumentDto> searchMulticritereNatifViaDto(BaDocumentSearchRequest request) {

        // Préparer les keywords pour SQL LIKE
        String[] keywords = null;
        if (request.getKeywords() != null && !request.getKeywords().isEmpty()) {
            keywords = request.getKeywords().stream()
                    .map(k -> "%" + k.toLowerCase() + "%")
                    .toArray(String[]::new);
        }

        // Conversion des listes en tableaux pour PostgreSQL
        String[] langues = request.getLangueIds() != null && !request.getLangueIds().isEmpty()
                ? request.getLangueIds().toArray(new String[0]) : null;

        String[] domaines = request.getDomaines() != null && !request.getDomaines().isEmpty()
                ? request.getDomaines().toArray(new String[0]) : null;

        String[] parties = request.getParties() != null && !request.getParties().isEmpty()
                ? request.getParties().toArray(new String[0]) : null;

        String nature = request.getNature() != null ? request.getNature().name() : null;

        List<BaDocument> results = documentRepository.searchDocumentStrict(
                request.getTypeId(),
                nature,
                langues,
                domaines,
                parties,
                keywords
        );

        return results.stream().map(mapper::maps).toList();
    }

    @Override
    public List<BaDocumentDto> searchMulticritereNatifLargeViaDto(BaDocumentSearchRequest request) {

        // Préparer les keywords pour SQL LIKE
        String[] keywords = null;
        if (request.getKeywords() != null && !request.getKeywords().isEmpty()) {
            keywords = request.getKeywords().stream()
                    .map(k -> "%" + k.toLowerCase() + "%")
                    .toArray(String[]::new);
        }

        // Conversion des listes en tableaux pour PostgreSQL
        String[] langues = request.getLangueIds() != null && !request.getLangueIds().isEmpty()
                ? request.getLangueIds().toArray(new String[0]) : null;

        String[] domaines = request.getDomaines() != null && !request.getDomaines().isEmpty()
                ? request.getDomaines().toArray(new String[0]) : null;

        String[] parties = request.getParties() != null && !request.getParties().isEmpty()
                ? request.getParties().toArray(new String[0]) : null;

        String nature = request.getNature() != null ? request.getNature().name() : null;

        List<BaDocument> results = documentRepository.searchDocumentLarge(
                request.getTypeId(),
                nature,
                langues,
                domaines,
                parties,
                keywords
        );

        return results.stream().map(mapper::maps).toList();
    }

    /**
     * Recherche obligatoire( Nature, type, domaine)
     * @param request: Dto
     * @return une liste de documents
     */

    @Override
    public List<BaDocumentDto> searchMulticritereNatifMixteViaDto(BaDocumentSearchRequest request) {

        // Préparer les keywords pour SQL LIKE
        String[] keywords = null;
        if (request.getKeywords() != null && !request.getKeywords().isEmpty()) {
            keywords = request.getKeywords().stream()
                    .map(k -> "%" + k.toLowerCase() + "%")
                    .toArray(String[]::new);
        }

        // Conversion des listes en tableaux pour PostgreSQL
        String[] langueIds = request.getLangueIds() != null && !request.getLangueIds().isEmpty()
                ? request.getLangueIds().toArray(new String[0]) : null;

        String[] domaines = request.getDomaines() != null && !request.getDomaines().isEmpty()
                ? request.getDomaines().toArray(new String[0]) : null;

        String[] parties = request.getParties() != null && !request.getParties().isEmpty()
                ? request.getParties().toArray(new String[0]) : null;

        String nature = request.getNature() != null ? request.getNature().name() : null;

        List<BaDocument> results = documentRepository.searchDocumentsMixte(
                request.getTypeId(),
                nature,
                parties,
                langueIds,
                domaines,
                keywords
        );

        return results.stream().map(mapper::maps).toList();
    }



    //===============================GESTION DES FICHIERS=====================

    /**
     * Joindre un fichier principal à un document
     * @param file: le fichier
     * @param fichierDto: dto du fichier
     * @return un Dto
     */
    @Override
    public BaFichierDto saveFichierPrincipal(MultipartFile file, BaFichierDto fichierDto) {
        // 1. Vérification fichier
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le fichier est vide ou manquant.");
        }

        // 2. Vérification de l'ID obligatoire
        if (fichierDto.getDocumentId() == null && fichierDto.getAffilieId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un ID (document ou affilié) est obligatoire.");
        }

        // 3. Sauvegarde physique
        String nomUnique = fileStorageService.saveFileDocumentPDF(file);

        // 4. Création de l'entité
        BaFichier baFichier = mapper.maps(fichierDto);
        baFichier.setId(BaUtils.randomUUID());
        baFichier.setUrl(nomUnique);
        baFichier.setLibelle(file.getOriginalFilename());

        // 5. Association selon le type de parent
        if (fichierDto.getDocumentId() != null) {
            BaDocument doc = documentRepository.findById(fichierDto.getDocumentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document introuvable."));
            baFichier.setAccord(doc);
            baFichier.setType(ETypeFichier.PRINCIPAL);
            baFichier.setAffilie(null);
        } else {
            BaDocumentAffilie docAffilie = documentAffilieRepository.findById(fichierDto.getAffilieId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document affilié introuvable."));
            baFichier.setAffilie(docAffilie);
            baFichier.setType(ETypeFichier.AFFILIE);
            baFichier.setAccord(null);
        }

        // 6. Sauvegarde en base
        BaFichier saved = fichierRepository.save(baFichier);

        // 7. Retour DTO
        return mapper.maps(saved);
    }

    /**
     * Supprime un fichier principal d'un traité ou accord.
     * @param fichierId: identifiant du personnel
     */
    @Override
    @Transactional
    public BaDocumentDto removeFichierFromAccord(String fichierId) {
        // Vérifier l'existence du fichier
        BaFichier fichier = fichierRepository.findById(fichierId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Fichier introuvable avec l'ID fourni."));

        BaDocument document = null;

        // Vérifier si le fichier est rattaché à un document principal
        if (fichier.getAccord() != null) {
            document = documentRepository.findById(fichier.getAccord().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Le document principal lié à ce fichier est introuvable."));

            if (!document.getFichiers().contains(fichier)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Le fichier n'est pas associé au document principal.");
            }

            // Retirer le fichier de la collection
            document.getFichiers().remove(fichier);
        }

        // Vérifier si le fichier est rattaché à un document affilié
        else if (fichier.getAffilie() != null) {
            BaDocumentAffilie documentAffilie = documentAffilieRepository.findById(fichier.getAffilie().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Le document affilié lié à ce fichier est introuvable."));

            if (!documentAffilie.getFichiers().contains(fichier)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Le fichier n'est pas associé au document affilié.");
            }

            // Retirer le fichier
            documentAffilie.getFichiers().remove(fichier);
            documentAffilieRepository.save(documentAffilie);
        }

        else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Le fichier n'est rattaché ni à un document principal ni à un document affilié.");
        }

        // ➡️ Supprimer le fichier physique sur disque AVANT suppression en base
        deletePhysicalFile(fichier.getUrl());

        // Supprimer l’entrée en base
        fichierRepository.delete(fichier);

        // Sauvegarder le document principal si trouvé
        if (document != null) {
            BaDocument updatedDoc = documentRepository.save(document);
            return mapper.maps(updatedDoc);
        }

        return null;
    }

    /**
     * Supprime le fichier physique du répertoire.
     */
    private void deletePhysicalFile(String fileName) {
        if (fileName == null) return;

        try {
            Path filePath = Paths.get(basePath).toAbsolutePath().normalize().resolve(fileName);
            Files.deleteIfExists(filePath); // supprime seulement si le fichier existe
        } catch (IOException e) {
            // Log erreur, mais ne pas bloquer la transaction DB
            log.error("Impossible de supprimer le fichier physique : " + fileName, e);
        }
    }


    //===============GESTION DES DOCUMENTS AFFILIES=============
    /**
     * Crée un nouveau traité ou accord.
     *
     * @param dto DTO contenant les informations l'accord ou traité.
     * @return L'accord ou traité créé sous forme de DTO.
     */
    @Override
    public BaDocumentAffilieDto createDocumentAffilieP(BaDocumentAffilieDto dto, MultipartFile file) {
        // 1. Mapper le DTO vers l’entité
        BaDocumentAffilie entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());
        // 2. Sauvegarder le document d'abord (pour générer l’ID et gérer les relations)
        documentAffilieRepository.save(entity);

        // 3. Si des fichiers sont fournis
        if (file != null && !file.isEmpty()) {
                // 3.1 Sauvegarder physiquement le fichier (retourne le nom unique)
                String savedFileName = fileStorageService.saveFileDocumentPDF(file);

                // 3.2 Créer une entité BaFichier associée au document
                BaFichier fichier = BaFichier.builder()
                        .id(BaUtils.randomUUID())
                        .libelle(file.getOriginalFilename()) // le vrai nom d’origine
                        .url(savedFileName) // chemin/nom unique du fichier sauvegardé
                        .affilie(entity) // relation ManyToOne
                        .accord(null)
                        .build();

                // 3.3 Ajouter au set de fichiers du document
                entity.getFichiers().add(fichier);

        }

        // 4. Sauvegarder encore pour persister les fichiers liés
        BaDocumentAffilie saved = documentAffilieRepository.save(entity);

        // 5. Logger
        logService.log(new BaLogDto(EAction.CREATE, "Création du document : " + dto.getIntituleAffilie()));

        // 6. Retourner le DTO
        return mapper.maps(saved);
    }

    @Override
    public BaDocumentAffilieDto createDocumentAffilie(BaDocumentAffilieDto dto, MultipartFile file) {
        // 1. Mapper le DTO vers l’entité
        BaDocumentAffilie entity = mapper.maps(dto);
        entity.setId(BaUtils.randomUUID());

        // ⚠️ Initialiser le Set de fichiers si ce n’est pas déjà fait
        // entity.getFichiers() peut être null après le mapping. On initialise donc avec new HashSet<>() pour éviter le NullPointerException
        if (entity.getFichiers() == null) {
            entity.setFichiers(new HashSet<>());
        }

        // 2. Sauvegarder le document affilié d'abord (pour générer l'ID)
        documentAffilieRepository.save(entity);

        // 3. Traiter le fichier associé (si présent)
        if (file != null && !file.isEmpty()) {
            // 3.1 Sauvegarder physiquement le fichier
            String savedFileName = fileStorageService.saveFileDocumentPDF(file);

            // 3.2 Créer l'entité BaFichier et l'associer au document affilié
            BaFichier fichier = BaFichier.builder()
                    .id(BaUtils.randomUUID())
                    .libelle(file.getOriginalFilename())
                    .url(savedFileName)
                    .type(ETypeFichier.AFFILIE)
                    .affilie(entity)  // lien ManyToOne vers le document affilié
                    .accord(null)     // pas associé au document principal
                    .build();

            // 3.3 Ajouter le fichier au Set du document affilié
            entity.getFichiers().add(fichier);
        }

        // 4. Sauvegarder à nouveau pour persister le fichier lié
        BaDocumentAffilie saved = documentAffilieRepository.save(entity);

        // 5. Log de l’action
        logService.log(new BaLogDto(EAction.CREATE, "Création du document affilié : " + dto.getIntituleAffilie()));

        // 6. Retourner le DTO correspondant
        return mapper.maps(saved);
    }
    /**
     * Récupérer tous les documents affiliés d’un document principal avec leurs fichiers
     *
     * @param documentId ID du document principal
     * @return Liste des DTOs des documents affiliés, chaque DTO contient la liste des fichiers
     */
    @Override
    public List<BaDocumentAffilieDto> getAllAffiliesWithFiles(String documentId) {
        // 1. Vérifier l’existence du document principal
        BaDocument principal = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Document principal introuvable avec l'ID : " + documentId));

        // 2. Récupérer tous les documents affiliés liés au document principal
        List<BaDocumentAffilie> affilies = documentAffilieRepository.findByAccord(principal);

        // 3. Logger l’action
        logService.log(new BaLogDto(EAction.VIEW,
                "Consultation des documents affiliés et fichiers du document : " + principal.getIntitule()));

        // 4. Mapper en DTO et inclure les fichiers
        return affilies.stream()
                .map(affilie -> {
                    BaDocumentAffilieDto dto = mapper.maps(affilie);

                    if (affilie.getFichiers() != null) {
                        Set<BaFichierDto> fichiersDto = affilie.getFichiers().stream()
                                .map(fichier -> {
                                    BaFichierDto fDto = new BaFichierDto();
                                    fDto.setId(fichier.getId());
                                    fDto.setLibelle(fichier.getLibelle());
                                    fDto.setUrl(fichier.getUrl());
                                    fDto.setAffilieId(affilie.getId());
                                    return fDto;
                                })
                                .collect(Collectors.toSet());
                        dto.setFichiers(fichiersDto);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * Fonction utilitaire pour la création des documents et documents affiliés
     *
     * @param file          : fichier à téléverser
     * @param savedFileName : le nom du nom à retourner
     * @param accord        : le
     */

    private void buildAndAttachFile(MultipartFile file, String savedFileName,
                                    BaDocument accord) {
        ETypeFichier type = (accord != null) ? ETypeFichier.PRINCIPAL : ETypeFichier.AFFILIE;

        BaFichier fichier = BaFichier.builder()
                .id(BaUtils.randomUUID())
                .libelle(file.getOriginalFilename())
                .url(savedFileName)
                .type(type)
                .accord(accord)
                .affilie(null)
                .build();

        if (accord != null) {
            accord.getFichiers().add(fichier);
        }

    }


    private void registerFileRollback(String filePath) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_ROLLED_BACK) {
                    // rollback → supprimer le fichier créé
                    new File(filePath).delete();
                }
            }
        });
    }

    private void registerFileDeletion(String filePath) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_COMMITTED) {
                    // suppression confirmée après commit
                    new File(filePath).delete();
                }
            }
        });
    }

    //========Gestion des de lecture des fichiers des Fichier==========

    /**
     * Récupère tous les fichiers associés à un document.
     */
    @Override
    public List<BaFichierDto> getFichiersByDocumentId(String documentId) {
        BaDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Document introuvable avec ID : " + documentId
                ));

        return document.getFichiers()
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les fichiers associés à un document.
     */
    @Override
    public List<BaFichierDto> getFichiersByDocumentAffilieId(String docId) {
         BaDocumentAffilie documentAffilie = documentAffilieRepository.findById(docId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Document introuvable avec ID : " + docId
                ));

        return documentAffilie.getFichiers()
                .stream()
                .map(mapper::maps)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les fichiers associés à un document affilié.
     */
    @Override
    public byte[] readAllByteOfFichier(String idFichier) {

        // 2. Lire le contenu du fichier stocké (le service reçoit l'URL unique du fichier)
        return fileStorageService.getFichier(idFichier);
    }

    @Override
public ResponseEntity<byte[]> telechargerFichier(String idFichier, boolean download) {


        // 1. Lire le contenu binaire
        byte[] fileBytes = fileStorageService.getFichier(idFichier);

// 2. Récupérer l'entité fichier
        BaFichier fichier = fichierRepository.findById(idFichier)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Fichier introuvable avec l'ID : " + idFichier));
        // 3. Nom du fichier (par défaut : document.pdf)
        String nomFichier = fichier.getLibelle() != null ? fichier.getLibelle() : "document.pdf";

        // 4. Préparer les headers HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        if (download) {
            headers.setContentDisposition(ContentDisposition.attachment().filename(nomFichier).build());
        } else {
            headers.setContentDisposition(ContentDisposition.inline().filename(nomFichier).build());
        }
        headers.setContentDisposition(ContentDisposition.inline().filename(nomFichier).build());


        // 5. Construire la réponse HTTP complète
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    /**
     * Statistique par Nature de document
     * @return: un objet de BaNatureStatistiqueDto
     */
    /**
     * Statistiques par nature de document
     */
    @Override
    public List<BaStatistique> getStatsByNature() {
        List<Object[]> results = documentRepository.countByNatureDocument();

        // mettre en map {nature -> count}
        Map<ENatureDocument, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        obj -> (ENatureDocument) obj[0],
                        obj -> (Long) obj[1]
                ));

        // retourner toutes les valeurs possibles de l'enum
        return Arrays.stream(ENatureDocument.values())
                .map(nature -> new BaStatistique(
                        nature.name(),
                        counts.getOrDefault(nature, 0L) // si absent → 0
                ))
                .collect(Collectors.toList());
    }

    /**
     * Statistiques par domaine
     */
    @Override
    public List<BaStatistique> getStatsByDomaine() {
        List<Object[]> results = documentRepository.countByDomaine();

        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));

        // On récupère TOUS les domaines existants
        return domaineRepository.findAll().stream()
                .map(d -> new BaStatistique(
                        d.getLibelle(),
                        counts.getOrDefault(d.getLibelle(), 0L)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Statistiques par partie prenante
     */
    @Override
    public List<BaStatistique> getStatsByPartiePrenante() {
        List<Object[]> results = documentRepository.countByPartiePrenante();

        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));

        // On récupère TOUTES les parties prenantes
        return partieRepository.findAll().stream()
                .map(p -> new BaStatistique(
                        p.getLibelle(),
                        counts.getOrDefault(p.getLibelle(), 0L)
                ))
                .collect(Collectors.toList());
    }
}

