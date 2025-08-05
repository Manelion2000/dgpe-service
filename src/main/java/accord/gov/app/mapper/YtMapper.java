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
    @Mappings({
            @Mapping(target = "idCategorie", source = "categorie.id"),
            @Mapping(target = "nomCategorie", source = "categorie.nom"),
    })
    BaProductDto maps(BaProduct entity);

    @InheritInverseConfiguration
    BaProduct maps(BaProductDto dto);

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
    @Mappings({})
    BaDocumentDto maps(BaDocument entity);

    @InheritInverseConfiguration
    BaDocument maps(BaDocumentDto dto);

    /**
     * Convertie le fichier en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({
            @Mapping(source = "pro.id", target = "idProfil"),
    })
    BaFichierDto maps(BaFichier entity);

    @InheritInverseConfiguration
    BaFichierDto maps(BaFichierDto dto);
/**
     * Convertie le document affilié en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
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
     * Convertie un parti en Dto
     * @param entity: entité
     * @return un dto
     */
    @Mappings({})
    BaTypeAccordDto maps(BaTypeAccord entity);

    @InheritInverseConfiguration
    BaTypeAccord maps(BaTypeAccordDto dto);

    /**
     * Mapping des log
     * @param entity: entité
     * @return un LogDto
     */


    @Mappings({})
    BaLogDto maps(BaLog entity);

    @InheritInverseConfiguration
    BaLog maps(BaLogDto dto);

    @Mappings({})
    BaCategorieDto maps(BaCategorie entity);

    @InheritInverseConfiguration
    BaCategorie maps(BaCategorieDto dto);

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
