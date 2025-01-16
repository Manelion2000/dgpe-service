package com.bakouan.app.mapper;

import com.bakouan.app.dto.*;
import com.bakouan.app.model.*;
import com.bakouan.app.utils.BaUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface YtMapper {

    /**
     * Convertir une entité demande en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(target = "idUser", source = "user.id"),
            @Mapping(target = "idMissionDiplomatique", source = "missionDiplomatique.id"),
            @Mapping(target = "pays", source = "missionDiplomatique.pays"),
            @Mapping(target="libelleMissionDiplomatique",source = "missionDiplomatique.libelle"),
            @Mapping(target = "documents", source = "documents")

    })
    BaDemandeDto maps(BaDemande entity);

    /**
     * Convertir une entité Personnel DGPE en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(target = "documents", source = "documents")
    })
    BaPersonneDgpeDto maps(BaPersonnelDgpe entity);

    @InheritInverseConfiguration
    BaPersonnelDgpe maps(BaPersonneDgpeDto dto);


    /**
     * Convertir une entité Document en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(target = "idDemande", source = "demande.id"),
    })
    BaDocumentDto maps(BaDocument entity);

    /**
     * Convertir une DTO Document en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaDocument maps(BaDocumentDto dto);

    /**
     * Convertir une entité Document en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(target="idPersonnel", source = "personnel.id")
    })
    BaPhotoPersonnelDto maps(BaPhotoPersonnel entity);

    /**
     * Convertir une DTO Document en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaPhotoPersonnel maps(BaPhotoPersonnelDto dto);


    /**
     * Convertir une entité user en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(source = "profil.id", target = "idProfil"),
            @Mapping(source = "profil.libelle", target = "libelleProfil"),
            @Mapping(source = "roles",   target="roles")
    })
    BaUserDto maps(BaUser entity);

    /**
     * Convertir une entité user en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({})
    BaRoleDto maps(BaRole entity);

    /**
     * Convertir une entité profil  en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({})
    BaProfilDto maps(BaProfil entity);

    /**
     * Convertir un DTO demande en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaDemande maps(BaDemandeDto dto);

    /**
     * Convertir un DTO user en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaUser maps(BaUserDto dto);

    /**
     * Convertir un DTO Role en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaRole maps(BaRoleDto dto);

    /**
     * Convertir un dto Profil en entité.
     *
     * @param dto
     * @return le dto
     */
    @InheritInverseConfiguration
    BaProfil maps(BaProfilDto dto);

    @Mappings({})
    BaLogDto maps(BaLog entity);

    @InheritInverseConfiguration
    BaLog maps(BaLogDto dto);

    /**
     * Convertir une entité BaMissionDiplomatique en Dto
     * @param entity
     * @return
     */
    @Mappings({})
    BaMissionDiplomatiqueDto maps(BaMissionDiplomatique entity);

    @InheritInverseConfiguration
    BaMissionDiplomatique maps(BaMissionDiplomatiqueDto dto);

    /**
     * After mapping method.
     *
     * @param dto    dto
     * @param entity entity
     */
    @AfterMapping()
    default void afterMapping(final BaUserDto dto,
                              @MappingTarget BaUser entity) {
        if (dto == null) {
            return;
        }

        if (BaUtils.isEmpty(dto.getIdProfil())) {
            entity.setProfil(null);
        }
    }

}
