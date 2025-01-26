package com.bakouan.app.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

    private String url;
@NotBlank(message = "l'id du membre est obligatoire")
    private String idMembre;
}
