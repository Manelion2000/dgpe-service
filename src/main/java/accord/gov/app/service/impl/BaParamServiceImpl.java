package accord.gov.app.service.impl;


import accord.gov.app.dto.*;
import accord.gov.app.enums.EAction;
import accord.gov.app.enums.EStatut;
import accord.gov.app.mapper.YtMapper;
import accord.gov.app.model.*;
import accord.gov.app.repositories.*;
import accord.gov.app.service.BaLogService;
import accord.gov.app.service.BaParamService;
import accord.gov.app.utils.BaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class BaParamServiceImpl implements BaParamService {

    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);
    private final BaLogService logService;
    private final BaTypeAccordRepository typeAccordRepository;
    private final BaTypeDocumentAffilieRepository typeDocumentAffilieRepository;
    private final BaLangueRepository langueRepository;
    private final BaDomaineRepository domaineRepository;
    private final BaPartieRepository partieRepository;
    private final BaDocumentRepository documentRepository;

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
    public BaDocumentDto createDocument(BaDocumentDto dto) {
        BaDocument entity = mapper.maps(dto);
        documentRepository.save(entity);
        logService.log(new BaLogDto(EAction.CREATE, "Création du document : " + dto.getIntitule()));
        return mapper.maps(entity);
    }
/**
 * Mise à jour du traité ou accord
 */

@Override
public BaDocumentDto updateDocument(String id, BaDocumentDto dto) {
    BaDocument entity = documentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document introuvable"));

    entity.setIntitule(dto.getIntitule());
    entity.setNote(dto.getNote());
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
    public BaDocumentDto getById(String id) {
        BaDocument entity = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document introuvable"));
        logService.log(new BaLogDto(EAction.VIEW, "Consultation du document ID: " + id));
        return mapper.maps(entity);
    }

    @Override
    public List<BaDocumentDto> getAllDocument() {
        List<BaDocument> list = documentRepository.findAll();
        logService.log(new BaLogDto(EAction.VIEW, "Consultation de la liste des documents"));
        return list.stream().map(mapper::maps).collect(Collectors.toList());
    }
}
