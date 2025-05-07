package com.bakouan.app.service;

import com.bakouan.app.dto.*;
import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.ETypeAutorisation;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BaAutorisationService {
    List<BaAutorisationSpecialeDto> findByType(ETypeAutorisation type);

    BaAutorisationSpecialeDto create(final BaAutorisationSpecialeDto autorisationSpecialeDto, MultipartFile noteVervale);

    void ValiderDemande(String id);

    BaAutorisationSpecialeDto uploadNoteVerbale(String autorisationId, MultipartFile noteVerbale);

    BaAutorisationSpecialeDto removeNoteVerbale(String autorisationId, String documentId);

    BaAutorisationSpecialeDto update(String id, BaAutorisationSpecialeDto autorisationSpecialeDto);

    void delete(final String id);
    BaAutorisationSpecialeDto findById(final String id);
    BaAutorisationSpecialeDto validateSt(final String id);
    BaAutorisationSpecialeDto rejectSt(final String id, BaAutorisationSpecialeDto auDto);
    BaAutorisationSpecialeDto validateDg(final String id);
    BaAutorisationSpecialeDto rejectDg(final String id);
    List<BaAutorisationSpecialeDto> findAll();

    List<BaAutorisationSpecialeDto> autorisationSpecialArchiv();

    List<BaAutorisationSpecialeDto> findAllByUser(final String userId);


    List<BaAutorisationSpecialeDto> findValiderParEtat(EEtatAutorisation etatAutorisation);

    BaDelegationMembreDto createMember(BaDelegationMembreDto dto);

    BaDelegationMembreDto createMember(BaDelegationMembreDto dto,
                                       List<BaDocumentPersonnelAutorisationSpecialDto> docDtoList,
                                       List<MultipartFile> files);

    @Transactional
    void deleteMember(String membreId);

    BaDelegationMembreDto addDocumentToMember(String membreId, MultipartFile file);

    BaDelegationMembreDto removeDocumentFromMember(String membreId, String documentId);

    BaDelegationMembreDto getMemberById(String membreId);

    List<BaDelegationMembreDto> getAllMembers();

    List<BaDelegationMembreDto> getMembersByAutorisationSpeciale(String autorisationSpecialeId);

    BaDocumentAutorisationSpecialDto uploadDocumentFinal(String autorisationId, MultipartFile fichierFinal);

    void deleteDocumentFinal(String autorisationId);
}
