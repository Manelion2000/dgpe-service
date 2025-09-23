package accord.gov.app.service;

import accord.gov.app.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaParamService {
    BaTypeAccordDto createTypeAccord(final BaTypeAccordDto dto);

    BaTypeAccordDto getAccordById(final String id);

    BaDocumentDto getById(String id);

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

    List<BaDocumentDto> getAllDocumentByStatutActive();

    List<BaDocumentDto> getAllDocumentByStatutArchives();

    List<BaDocumentDto> searchMulticritere(String typeId,
                                           List<String> langueIds,
                                           List<String> domaineIds,
                                           List<String> partieIds,
                                           List<String> motsCles,
                                           String nature);

    List<BaDocumentDto> searchMulticritereViaDto(BaDocumentSearchRequest request);

    BaFichierDto saveFichierPrincipal(MultipartFile file, BaFichierDto fichierDto);

    BaDocumentDto removeFichierFromAccord(String documentId, String fichierId);
}
