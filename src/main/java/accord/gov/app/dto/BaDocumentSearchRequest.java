package accord.gov.app.dto;

import accord.gov.app.enums.ENatureDocument;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 15/09/2025 à 01:48
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaDocumentSearchRequest {

    private String typeId; // ID du type de document

    // Pour la nature, on utilise un Enum et on ajoute @JsonDeserialize
    @JsonDeserialize(using = ENatureDocumentDeserializer.class)
    private ENatureDocument nature;

    private List<String> langueIds = new ArrayList<>();
    private List<String> parties = new ArrayList<>();
    private List<String> domaines = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
}
