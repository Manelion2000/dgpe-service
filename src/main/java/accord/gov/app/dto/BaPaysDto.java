package accord.gov.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 11/07/2025 à 16:09
 */
@Getter
@Setter
@Builder
public class BaPaysDto {
    private String id;

    @NotBlank(message = "le libelle du pays ne doit pas être vide")
    private String libelle;
}
