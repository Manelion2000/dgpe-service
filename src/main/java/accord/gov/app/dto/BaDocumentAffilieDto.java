package accord.gov.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @project : traiteAccordService
 * @since : 07/07/2025 à 18:59
 */
@Getter @Setter @Builder
public class BaDocumentAffilieDto {
    private String id;

    @NotBlank(message = "intitulé vide")
    private String intituleAffilie;

    @NotBlank(message = "le code de la boite es obligatoire")
    private String codeBoite;

    @NotBlank(message = "le titre du document est obligatoire")
    private String titre;

    private LocalDate dateSignature;

    private LocalDate dateRatification;

    private String documentId;

    @NotBlank(message = "le type de document affilé doit être precisé")
    private String typeDocumentAffilieId;
}
