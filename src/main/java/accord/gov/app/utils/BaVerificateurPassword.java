package accord.gov.app.utils;

import java.security.SecureRandom;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 07/10/2025 à 15:04
 */
public class BaVerificateurPassword {
    private static int nbr_maj(String chaine) {
        int compteur = 0;
        for (int i = 0; i < chaine.length(); i++) {
            char ch = chaine.charAt(i);
            if (Character.isLowerCase(ch))
                compteur++;
        }
        return compteur;
    }

    private static int nbr_min(String chaine) {
        int compteur = 0;
        for (int i = 0; i < chaine.length(); i++) {
            char ch = chaine.charAt(i);
            if (Character.isUpperCase(ch))
                compteur++;
        }
        return compteur;
    }

    boolean resultat(String chaine) {
        Boolean etat = false;
        if (nbr_maj(chaine) > 0 && nbr_min(chaine) > 0 && chaine.length() > 7) {
            etat = true;
        }
        return etat;
    }

    public static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }

}
