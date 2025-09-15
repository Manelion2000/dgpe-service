package accord.gov.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 15/09/2025 à 01:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaDocumentSearchRequest {

    private String typeId;             // ex: "bilateral"
    private String langueId;           // ex: "fr"
    private String nature;             // ex: "traite"
    private List<String> parties;      // ex: ["Burkina Faso", "Iran"]
    private List<String> domaines;     // ex: ["sécurité", "militaire"]
    private List<String> keywords;     // ex: ["coopération", "défense"] au niveau de l'intitulé
}
