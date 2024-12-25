package com.bakouan.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaPersonneDgpeDto {

    private String id ;

    private String libelle;

    private String url;

    @NotBlank(message = "la fonction est obligatoire")
    private String fonction;

    private String paragraphe1;

    private String paragraphe2;

    private String paragraphe3;

    private String paragraphe4;
}
