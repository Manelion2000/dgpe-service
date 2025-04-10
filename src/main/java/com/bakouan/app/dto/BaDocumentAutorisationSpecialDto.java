package com.bakouan.app.dto;

import com.bakouan.app.enums.EDocument;
import com.bakouan.app.enums.EDocumentAutorisation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaDocumentAutorisationSpecialDto {
    private String id;

    private String libelle;

    private String url;

    private String idAutorisationSpeciale;

}
