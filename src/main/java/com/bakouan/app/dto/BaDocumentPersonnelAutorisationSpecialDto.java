package com.bakouan.app.dto;

import com.bakouan.app.enums.EDocumentAutorisation;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaDocumentPersonnelAutorisationSpecialDto {

    private String id;

    @NotBlank(message = "le libelle est obligatoire")
    private String libelle;

    @NotNull(message="le type de document est obligatoire")
    private EDocumentAutorisation typeDocument;

    private String url;
    @NotBlank(message = "l'id du membre est obligatoire")
    private String idMembre;
}
