package accord.gov.app.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 06/09/2025 à 02:22
 */
public class BaTypeDocumentAffilieDto {
    private String id;

    @NotBlank(message = "le libelle de la partie est obligatoire")
    private String libelle;
}
