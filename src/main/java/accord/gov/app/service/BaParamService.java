package accord.gov.app.service;

import accord.gov.app.dto.*;
import accord.gov.app.enums.EConfidentiel;
import accord.gov.app.enums.EStatut;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaParamService {
    BaTypeAccordDto createTypeAccord(final BaTypeAccordDto dto);

    BaTypeAccordDto getAccordById(final String id);

    BaDocumentDto getByDocumentById(String id);

    List<BaTypeAccordDto> getAlTypeAccord();

    BaTypeAccordDto updateTypeAccord(final String id,final  BaTypeAccordDto dto);

    void  deleteTypeAccord(final String id);

    BaTypeDocumentAffilieDto createTypeDocumentAffilie(final BaTypeDocumentAffilieDto dto);

    BaTypeDocumentAffilieDto getTypeDocumentAffilieById(final String id);

    BaTypeDocumentAffilieDto getTypeAccordByLibelle(final String libelle);

    List<BaTypeDocumentAffilieDto> getAllTypeDocumentAffilie();

    BaTypeDocumentAffilieDto updateTypeDocumentAffilie(final String id, final  BaTypeDocumentAffilieDto dto);

    void  deleteTypeDocumentAffilie(final String id);

    BaLangueDto createLangue(final BaLangueDto dto);

    List<BaLangueDto> getAllLangue();

    BaLangueDto getLangueById(String id);

    BaLangueDto updateTypeAccord(final String id, final  BaLangueDto dto);

    void  deleteLangue(final String id);

    BaDomaineDto createDomaine(final BaDomaineDto dto);

    BaDomaineDto getDomaineById(final String id);

    List<BaDomaineDto> getAllDomaine();

    BaDomaineDto updateDomaine(final String id,final  BaDomaineDto dto);

    void  deleteDomaine(final String id);

    BaPartieDto createPartie(BaPartieDto dto);

    BaPartieDto getPartieById(final String id);

    List<BaPartieDto> getAllPartie();

    BaPartieDto updatePartie(final String id,final  BaPartieDto dto);

    void  deletePartie(final String id,final BaPartieDto dto);

    BaPartieDto getPartieByLibelle(final String libelle,final BaPartieDto dto);

    BaDocumentDto createDocument(BaDocumentDto dto,List<MultipartFile> files);

    BaDocumentDto updateDocumentWithFiles(String documentId,
                                          List<MultipartFile> newFiles,
                                          List<String> filesToDelete) throws IOException;

    BaDocumentDto updateDocument(String id, BaDocumentDto dto);

    void deleteDocument(String id);

    List<BaDocumentDto> getAllDocument();

    //List<BaDocumentDto> getAllDocumentByStatutActive();

    //List<BaDocumentDto> getAllDocumentByStatutActiveEtConfidentiel();

    //List<BaDocumentDto> getAllDocumentByStatutArchives();

    List<BaDocumentDto> getAllDocumentsByStatutAndConfidentialite(EStatut statut, EConfidentiel confidentiel);

    List<BaDocumentDto> searchMulticritereViaDto(BaDocumentSearchRequest request);

    BaDocumentDto createDocumentP(BaDocumentDto dto, List<MultipartFile> files);

    List<BaDocumentDto> searchMulticritereNatifViaDto(BaDocumentSearchRequest request);

    BaFichierDto saveFichierPrincipal(MultipartFile file, BaFichierDto fichierDto);

    BaDocumentDto removeFichierFromAccord(String fichierId);

    BaDocumentAffilieDto createDocumentAffilie(BaDocumentAffilieDto dto, MultipartFile file);

    BaDocumentAffilieDto createDocumentAffilieP(BaDocumentAffilieDto dto, MultipartFile file);

    List<BaDocumentAffilieDto> getAllAffiliesWithFiles(String documentId);

    List<BaFichierDto> getFichiersByDocumentId(String documentId);

    List<BaFichierDto> getFichiersByDocumentAffilieId(String docId);

    //========Gestion des de lecture des fichiers des Fichier==========
    byte[] readAllByteOfFichier(String idFichier);

    ResponseEntity<byte[]> telechargerFichier(String idFichier, boolean download);

    List<BaStatistique> getStatsByNature();

    List<BaStatistique> getStatsByDomaine();

    List<BaStatistique> getStatsByPartiePrenante();
}
