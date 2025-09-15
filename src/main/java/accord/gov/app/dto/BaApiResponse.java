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
 * @since : 10/09/2025 à 11:05
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaApiResponse<T> {
    private String message;
    private int status;
    private T data;
}