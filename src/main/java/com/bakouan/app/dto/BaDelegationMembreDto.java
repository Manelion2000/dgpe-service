package com.bakouan.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class BaDelegationMembreDto {
    private String id;

    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "le prenom est obligatoire")
    private String prenom;

    @NotBlank(message = "la fonction est obligatoire")
    private String fonction;

    private String idAutorisationSpeciale;

    private Set< BaDocumentPersonnelAutorisationSpecialDto> documents ;
}
