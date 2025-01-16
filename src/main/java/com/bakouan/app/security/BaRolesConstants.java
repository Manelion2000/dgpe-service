package com.bakouan.app.security;

/**
 * Constants for Spring Security authorities.
 */
public final class BaRolesConstants {

    /**
     * L'administrateur de la plateforme.
     */
    public static final String BA_ADMIN = "BA_ADMIN";

    /**
     * Tout utilisateur disposant d'un compte(role commun).
     */
    public static final String BA_CONNECT = "BA_CONNECT";

    /**
     * Tout utilisateur disposant d'un compte sécretaire.
     */
    public static final String BA_SECRETAIRE = "BA_SECRETAIRE";


    /**
     * Tout utilisateur disposant d'un compte dans le service diplomatique.
     */
    public static final String BA_SERVICE_DIPLOMATIQUE = "BA_ST_DP";

    /**
     * Tout utilisateur disposant d'un compte dans le service de traitement des cartes aux salons.
     */
    public static final String BA_SERVICE_ACCES_SALON = "BA_ST_AC";

    /**
     * Tout utilisateur disposant d'un compte du directeur géneral.
     */
    public static final String BA_DIRECTEUR = "BA_ST_DG";

    private BaRolesConstants() {
    }


}
