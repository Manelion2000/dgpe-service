package accord.gov.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 04/09/2025 à 13:55
 */
public enum ETypePartie {
    ETAT("État"),
    OIG("Organisation Inter-Gouvernementale"),
    ONG("Organisation Non Gouvernementale"),
    AUTRE_ENTITE("Autre entité");

    private final String libelle;

    ETypePartie(String libelle) {
        this.libelle = libelle;
    }
  /* @JsonValue
    public String getLibelle() {
        return libelle;
    }

    // Conversion String → Enum, compatible avec le libellé du front
    @JsonCreator
    public static ETypePartie fromLibelle(String libelle) {
        for (ETypePartie t : ETypePartie.values()) {
            if (t.libelle.equalsIgnoreCase(libelle)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Type de partie invalide : " + libelle);
    }*/
}