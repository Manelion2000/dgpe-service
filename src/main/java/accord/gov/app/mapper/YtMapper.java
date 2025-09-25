package accord.gov.app.mapper;

import accord.gov.app.dto.*;
import accord.gov.app.model.*;
import accord.gov.app.utils.BaUtils;
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
     * Entity pays en Dto
     */
    BaPaysDto maps(BaPays entity);
    @InheritInverseConfiguration
    BaPays maps(BaPaysDto dto);

    /**
     * Convertie le document en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({
            @Mapping(source = "domaines", target = "domaines"),
            @Mapping(source = "langues", target = "langues"),
            @Mapping(source = "parties", target = "parties"),
            @Mapping(source = "typeDocument.id",target ="typeDocumentId")
    })
    BaDocumentDto maps(BaDocument entity);

    @InheritInverseConfiguration
    BaDocument maps(BaDocumentDto dto);

    /**
     * Convertie le fichier en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({
            @Mapping(source = "accord.id", target = "documentId"),
            @Mapping(source = "affilie.id",target = "affilieId")
    })
    BaFichierDto maps(BaFichier entity);

    @InheritInverseConfiguration
    BaFichier maps(BaFichierDto dto);
/**
     * Convertie le document affilié en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({
            @Mapping(source = "typeDocumentAffilie.id", target = "typeDocumentAffilieId"),
            @Mapping(source = "accord.id", target = "documentId")
    })
    BaDocumentAffilieDto maps(BaDocumentAffilie entity);

    @InheritInverseConfiguration
    BaDocumentAffilie maps(BaDocumentAffilieDto dto);

    /**
     * Convertie un domaine en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaDomaineDto maps(BaDomaine entity);

    @InheritInverseConfiguration
    BaDomaine maps(BaDomaineDto dto);

    /**
     * Convertie un parti en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaPartieDto maps(BaPartie entity);

    @InheritInverseConfiguration
    BaPartie maps(BaPartieDto dto);

    /**
     * Convertie un parti en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaLangueDto maps(BaLangue entity);

    @InheritInverseConfiguration
    BaLangue maps(BaLangueDto dto);

    /**
     * Convertie un type d'accord en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaTypeAccordDto maps(BaTypeAccord entity);

    @InheritInverseConfiguration
    BaTypeAccord maps(BaTypeAccordDto dto);
    /**
     * Convertie un type  en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaTypeDocumentAffilieDto maps(BaTypeDocumentAffilie entity);

    @InheritInverseConfiguration
    BaTypeDocumentAffilie maps(BaTypeDocumentAffilieDto dto);

    /**
     * Mapping des log
     * @param entity: entité
     * @return un LogDto
     */


    @Mappings({})
    BaLogDto maps(BaLog entity);

    @InheritInverseConfiguration
    BaLog maps(BaLogDto dto);


    /**
     * Convertir une entité user en DTO.
     *
     * @param entity
     * @return le dto
     */
    @Mappings({
            @Mapping(source = "profil.id", target = "idProfil"),
            @Mapping(source = "profil.libelle", target = "libelleProfil"),
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
