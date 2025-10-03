package accord.gov.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 30/09/2025 à 23:24
 */
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor
public class BaStatistique {
    private  String libelle;
    private Long nombre;
}
