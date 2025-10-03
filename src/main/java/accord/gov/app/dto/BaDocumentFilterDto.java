package accord.gov.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 02/10/2025 à 22:41
 */
@Getter @Setter
public class BaDocumentFilterDto {
    private Map<String, Object> rawFilters;
}
