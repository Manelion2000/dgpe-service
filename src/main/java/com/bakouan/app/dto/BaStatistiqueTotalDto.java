package com.bakouan.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor
@Setter @AllArgsConstructor
public class BaStatistiqueTotalDto {
    private Long total;
    private Long encours;
    private Long valide;
    private Long rejete;
    private Long enProduction;
    private Long delivre;
}
