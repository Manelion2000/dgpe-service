package com.bakouan.app.dto;
import com.bakouan.app.enums.EEtatAutorisation;
import com.bakouan.app.enums.ETypeAutorisation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaAutorisationSpecialeDto {
    private String id;

    private String numDemande;

    @PastOrPresent(message = "La date de demande ne peut être dans le futur")
    private LocalDate dateDemande;

    @FutureOrPresent(message = "La date de d'arrivé doit être aujourd'hui ou dans le futur")
    private LocalDate dateArrivee;

    @FutureOrPresent(message = "La date de départ doit être aujourd'hui ou dans le futur")
    private LocalDate dateDepart;

    private EEtatAutorisation etat;

    private String idMissionDiplomatique;

    private String libelleMissionDiplomatique;

    private String pays;

    private String userId;

    private String motifRejet;

    private LocalDate dateRejet;

    private LocalDate dateValidation;

    @NotNull(message = "Le type d'autorisation est obligatoire")
    private ETypeAutorisation typeAutorisation;

    private Set<BaDocumentAutorisationSpecialDto> documents;

    private BaDocumentAutorisationSpecialDto documentFinal;

    private Set<BaDelegationMembreDto> delegation;
}

