package com.bakouan.app.dto;
import com.bakouan.app.enums.EEtatAutorisation;
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
    @PastOrPresent(message = "La date de demande ne peut être dans le futur")
    private LocalDate dateDemande;

    private LocalDate dateArrivee;

    private LocalDate dateDepart;

    @NotNull(message = "L'etat de l'autorisation est obligatoire")
    private EEtatAutorisation etat;// EN_ATTENTE, VALIDE, REJETE

    private String idMissionDiplomatique;

    private String libelleMissionDiplomatique;

    private String pays;

    private String motifRejet;

    private LocalDate dateRejet;

    private LocalDate dateValidation;

    private Set<BaDocumentAutorisationSpecialDto> documents;

    private Set<BaDelegationMembreDto> delegation;


}
