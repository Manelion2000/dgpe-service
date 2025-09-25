package accord.gov.app.dto;

import lombok.*;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 14/07/2025 à 13:08
 */
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor
@Builder
public class BaFichierDto {
    private String id;
    private String libelle;
    private String url;
    private String documentId;
    private String affilieId;

}
