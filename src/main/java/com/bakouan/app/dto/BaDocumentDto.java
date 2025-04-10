package com.bakouan.app.dto;
import com.bakouan.app.enums.EDocument;
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

    private String numDocument;

    private Integer ordre;

    private String idDemande;

    @NotNull(message="le type de document est obligatoire")
    private EDocument typeDocument;
}
