package com.bakouan.app.dto;

import com.bakouan.app.enums.EDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BaDocumentDto {

    private String id;

    private String libelle;

    private String url;

    @NotBlank(message = "le numéro du document est obligatoire")
    private String numDocument;

    private String idDemande;

    @NotNull(message="le type de document est obligatoire")
    private EDocument typeDocument;
}
