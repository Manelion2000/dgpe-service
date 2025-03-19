package com.bakouan.app.service;

import com.bakouan.app.dto.BaAutorisationSpecialeDto;
import com.bakouan.app.dto.BaDelegationMembreDto;
import com.bakouan.app.enums.EEtatAutorisation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BaAutorisationService {
    BaAutorisationSpecialeDto create(final BaAutorisationSpecialeDto autorisationSpecialeDto, MultipartFile noteVervale);

    BaAutorisationSpecialeDto uploadNoteVerbale(String autorisationId, MultipartFile noteVerbale);

    BaAutorisationSpecialeDto removeNoteVerbale(String autorisationId, String documentId);

    BaAutorisationSpecialeDto update(String id, BaAutorisationSpecialeDto autorisationSpecialeDto);

    void delete(final String id);
    BaAutorisationSpecialeDto findById(final String id);
    BaAutorisationSpecialeDto validateSt(final String id);
    BaAutorisationSpecialeDto rejectSt(final String id);
    BaAutorisationSpecialeDto validateDg(final String id);
    BaAutorisationSpecialeDto rejectDg(final String id);
    List<BaAutorisationSpecialeDto> findAll();

    List<BaAutorisationSpecialeDto> autorisationSpecialArchiv();

    List<BaAutorisationSpecialeDto> findAllByUser(final String userId);


    List<BaAutorisationSpecialeDto> findValiderParEtat(EEtatAutorisation etatAutorisation);

    BaDelegationMembreDto createMember(BaDelegationMembreDto dto);

    BaDelegationMembreDto addDocumentToMember(String membreId, MultipartFile file);

    BaDelegationMembreDto removeDocumentFromMember(String membreId, String documentId);

    BaDelegationMembreDto getMemberById(String membreId);

    List<BaDelegationMembreDto> getAllMembers();
}
