package accord.gov.app.dto;

import accord.gov.app.model.BaFichier;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @project : traiteAccordService
 * @since : 07/07/2025 à 18:59
 */
@Getter @Setter  @NoArgsConstructor @AllArgsConstructor
@Builder
public class BaDocumentAffilieDto {
    private String id;

    @NotBlank(message = "intitulé vide")
    private String intituleAffilie;

    @NotBlank(message = "le code de la boite es obligatoire")
    private String codeBoite;

    private LocalDate dateSignature;

    private LocalDate dateRatification;

    private String documentId;

    private Set<BaFichierDto> fichierDtos;

    @NotBlank(message = "le type de document affilé doit être precisé")
    private String typeDocumentAffilieId;
}
