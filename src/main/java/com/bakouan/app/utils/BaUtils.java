package com.bakouan.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.time.Year;

@Slf4j
public class BaUtils {
    /**
     * Fontion pour generer le numero de démande
     * @param typeCarte : type de carte
     * @param sequence
     * @return
     */
    public static String generateNumeroDemande(String typeCarte, long sequence) {
        String year = String.valueOf(Year.now().getValue()).substring(1); // Récupère les deux derniers chiffres de l'année
        return String.format("BF-%s%s-%03d", typeCarte, year, sequence);
    }

    /**
     * Genère et retourne un idantifiant unique.
     *
     * @return UUID en String
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * Verifie si une chaine de caractère est vide.
     *
     * @param str
     * @return un boolean
     */
    public static boolean isEmpty(final String str) {
        return !StringUtils.hasText(str);
    }


    /**
     * Générer random number.
     *
     * @param taille : la taille du nombre
     * @return String: le nombre généré
     */
    public static String numberGenerator(final int taille) {
        String caracteres = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < taille; i++) {
            int index = (int) (caracteres.length() * Math.random());
            stringBuilder.append(caracteres.charAt(index));
        }
        return stringBuilder.toString();
    }

    /**
     * Extraire l'adresse IP de l'objet d'une requête.
     *
     * @param request
     * @return l'adresse
     */
    public static String retrieveIP(final HttpServletRequest request) {
        String frontendIpAddress = request.getHeader("X-Forwarded-For");
        if (frontendIpAddress != null && !frontendIpAddress.isEmpty()) {
            String[] splits = frontendIpAddress.split(",");
            return splits.length > 0 ? splits[0] : request.getRemoteAddr();
        } else {
            return request.getRemoteAddr();
        }
    }


    /**
     * Converts the generate DTO to a json inputStream.
     *
     * @param dto dto
     * @return InputStream
     * @throws IOException
     */
    public static InputStream convertDtoToInputStream(final Object dto) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Java object to JSON string
        String jsonString = mapper.writeValueAsString(dto);
        log.debug("Json String: " + jsonString);
        //use ByteArrayInputStream to get the bytes of the String and convert them to InputStream.
        return new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Create and store file from byte array.
     *
     * @param byteArray
     * @param filePath
     */
    public static void createFileFromByteArray(final byte[] byteArray,
                                               final String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(byteArray);
            log.debug("File created successfully at: " + file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing to file", e);
        }
    }

    public static String getMonthLabel(int month) {
        return switch (month) {
            case 1 -> "Janvier";
            case 2 -> "Février";
            case 3 -> "Mars";
            case 4 -> "Avril";
            case 5 -> "Mai";
            case 6 -> "Juin";
            case 7 -> "Juillet";
            case 8 -> "Août";
            case 9 -> "Septembre";
            case 10 -> "Octobre";
            case 11 -> "Novembre";
            case 12 -> "Décembre";
            default -> "Inconnu";
        };
    }

}
