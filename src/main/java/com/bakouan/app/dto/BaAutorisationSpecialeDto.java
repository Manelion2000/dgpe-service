package com.bakouan.app.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import java.util.Set;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaAutorisationSpecialeDto {
    private String id;

    @NotNull(message = "La date d'arrivée est obligatoire")
    private LocalDate dateArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    @NotNull(message = "L'etat de l'autorisation est obligatoire")
    private String etat;// EN_ATTENTE, VALIDE, REJETE

    private Set<BaDocumentAutorisationSpecialDto> documents;

    private Set<BaDelegationMembreDto> delegation;
}
