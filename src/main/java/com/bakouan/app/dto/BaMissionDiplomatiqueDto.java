package com.bakouan.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter @Setter
public class BaMissionDiplomatiqueDto {

    private String id;

    private String libelle;

    private String pays;

    private String nationalite;

    private Boolean reciprocite=false;
}
