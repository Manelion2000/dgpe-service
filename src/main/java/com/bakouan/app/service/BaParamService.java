package com.bakouan.app.service;

import com.bakouan.app.dto.*;
import com.bakouan.app.enums.ECarte;
import com.bakouan.app.enums.EStatus;
import com.bakouan.app.enums.ETypeDemandeur;
import com.bakouan.app.model.BaDocument;
import com.bakouan.app.model.BaPersonnelDgpe;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BaParamService {


    List<BaDemandeDto> getAllDemandes();

    List<BaDemandeDto> getAllDemandesArchive();

    List<BaDemandeDto> getDemandeValider();

    List<BaDemandeDto> getDemandeValiderDg();

    List<BaDemandeDto> getDemandeRejette();

    List<BaDemandeDto> getDemandeEncours();

    List<BaDemandeDto> getDemandeProduit();

    List<BaDemandeDto> getDemandeRetirer();

    List<BaDemandeDto> getDemandeSalonOfficiel();

    List<BaDemandeDto> getDemandeTypeDemandeur(ETypeDemandeur typeDemndeur);

    List<BaDemandeDto> getDemandeSalonDiplomatique();

    List<BaDemandeDto> getDemandeParTypeEtStatus(ECarte eCarte, EStatus eStatus);

    BaDemandeDto getDemandeByid(String id);

    BaDemandeDto createDemande(BaDemandeDto demandeDto);

    BaDemandeDto validerDemande(String id, BaDemandeDto demandeDtoDto);

    BaDemandeDto validerDemandeParDg(String id, BaDemandeDto demandeDtoDto);

    BaDemandeDto produireDemande(String id, BaDemandeDto demandeDtoDto);

    BaDemandeDto retirerDemande(String id, BaDemandeDto demandeDtoDto);

    BaDemandeDto rejeterDemande(String id, BaDemandeDto demandeDtoDto);

    BaDemandeDto rejeterDemandeParDg(String id, BaDemandeDto demandeDtoDto);

    BaDocument saveDocument(MultipartFile file, BaDocumentDto documentDto);

    //BaDocument savePhotoPersonnel(MultipartFile file, BaDocumentDto documentDto);

    BaPhotoPersonnelDto savePhotoPersonnel(MultipartFile file, BaPhotoPersonnelDto photoDto);

    BaPersonneDgpeDto removeDocumentFromPersonnel(String demandeId, String documentId);

    List<BaDocumentDto> getDocumentsByDemande(String idDemande);

    void removeDocumentFromDemande(String demandeId, String documentId);

    void deleteDocumentLogique(String id);

    void deletePhysique(String id);

    BaDemandeDto updateDemande(String id, @Valid BaDemandeDto demandeDto);

    BaDocumentDto updateDocument(String documentId, BaDocumentDto documentDto);

    BaMissionDiplomatiqueDto createMission(BaMissionDiplomatiqueDto missionDto);

    BaMissionDiplomatiqueDto updateMission(String id, BaMissionDiplomatiqueDto missionDto);

    void archiveMission(String id);

    List<BaMissionDiplomatiqueDto> getAllMissions();

    BaMissionDiplomatiqueDto getMissionById(String id);

    BaPersonneDgpeDto savePersonnel(BaPersonneDgpeDto personnelDto);

    BaPersonneDgpeDto updatePersonnel(String id, BaPersonneDgpeDto personnelDto);

    BaPersonneDgpeDto archiverPersonnel(String id, BaPersonneDgpeDto personnelDto);

    void deletePersonnel(String id);

    List<BaPersonneDgpeDto> getAllPersonnel();

    List<BaPersonneDgpeDto> getAllPersonnelArchiver();

    BaPersonneDgpeDto getPersonnelById(String id);

    List<BaStatistiquesDto> getDemandesByStatus();

    List<BaStatistiquesDto> getDemandesByCarte();

    List<BaStatistiquesDto> getDemandesByMonth();
}
