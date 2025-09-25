package accord.gov.app.dto;

import accord.gov.app.enums.EConfidentiel;
import accord.gov.app.enums.EEtatEnVigeur;
import accord.gov.app.enums.ENatureDocument;
import accord.gov.app.model.BaDomaine;
import accord.gov.app.model.BaLangue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @project : traiteAccordService
 * @since : 07/07/2025 à 18:58
 */
@Getter
@Setter
public class BaDocumentDto {
    private String id;

    @NotBlank(message = "Intitulé du document est obligatoire.")
    @Size(min = 5, max = 400, message = "L'intitulé doit contenir entre 5 et 255 caractères.")
    private String intitule;

    @Size(max = 500, message = "La note ne doit pas dépasser 500 caractères.")
    private String cote;

    @Size(max = 100, message = "Les mots-clés ne doivent pas dépasser 100 caractères.")
    private String motCle;

    @Size(max = 2000, message = "Le résumé ne doit pas dépasser 2000 caractères.")
    private String resume;

    private String codeBoite;

    private EEtatEnVigeur etatAccordVigueur;

    @NotNull(message = "Le niveau de confidentialité est obligatoire.")
    private EConfidentiel confidentialite = EConfidentiel.NON;

    private LocalDate dateAdoption;

    private LocalDate dateSignature;

    private LocalDate dateEntreeVigueur;

    private LocalDate dateRatification;

    @Size(max = 255)
    private String lieuSignature;

    @NotNull(message = "La nature du document est obligatoire.")
    private ENatureDocument natureDocument;


    @NotBlank(message = "L'identifiant du type de document est obligatoire.")
    private String typeDocumentId;

    @NotEmpty(message = "Au moins une partie prenante doit être sélectionnée.")
    private Set<BaPartieDto> parties;

    @NotEmpty(message = "Au moins un domaine doit être sélectionnée.")
    private Set< BaDomaineDto> domaines;

    @NotEmpty(message = "Au moins une langue prenante doit être sélectionnée.")
    private Set<BaLangueDto> langues;
}
