package accord.gov.app.service;

import accord.gov.app.repositories.BaFichierRepository;
import accord.gov.app.utils.BaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

/**
 * Classe de gestion du stockage des fichiers dans le système de fichier.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class BaFileStorageService {

    @Value("${app.storage.path}")
    private String basePath;
    private BaFichierRepository fichierRepository;

    /**
     * Récupérer le contenu d'un fichier.
     *
     * @param idDoc identifiant du fichier.
     * @return un tableau de byte
     */
    public byte[] get(final String idDoc) {
        File file = new File(basePath + File.separator + idDoc);
        try {
            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            } else {
                log.debug("Fichier inexistant : {}", idDoc);
                return new byte[]{};
            }
        } catch (IOException e) {
            log.error("Erreur de chargement du fichier : " + idDoc, e);
            return new byte[]{};
        }
    }

    /**
     * Enregistrer un document.
     *
     * @param content le contenu du fichier avec les metadata
     * @return l'id généré après l'enregistrement
     */
    public String save(final byte[] content) {
        String id = BaUtils.randomUUID();
        if (content != null && content.length > 0) {
            File file = new File(basePath + File.separator + id);

            try {
                log.debug("Saving File :  {} in : {}", id, file.getAbsolutePath());

                final Path path = Path.of(basePath);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }

                Files.write(file.toPath(), content,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                return id;
            } catch (IOException e) {
                log.error("Erreur d'enregistrement du fichier", e);
            }
        }
        return null;
    }

    /**
     * Mettre à jour un document.
     *
     * @param idDocument identifiant de l'imgae
     * @param content    le contenu du fichier avec les metadata
     * @return l'id généré après l'enregistrement
     */
    public boolean update(final String idDocument, final byte[] content) {

        if (content != null && content.length > 0) {
            File file = new File(basePath + File.separator + idDocument);
            try {
                log.debug("Saving File :  {} in : {}", idDocument, file.getAbsolutePath());
                Files.write(file.toPath(), content,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                return true;
            } catch (IOException e) {
                log.error("Erreur de modification du fichier", e);
            }
        }
        return false;
    }

    /**
     * Supprimer un fichier.
     *
     * @param idDocument identifiant du fichier
     * @return <code>true</code>, si la suppression réussi
     */
    public boolean remove(final String idDocument) {
        log.warn("Removing file {}", idDocument);
        File file = new File(basePath + File.separator + idDocument);
        try {
            if (file.exists()) {
                Files.delete(file.toPath());
            }
            return true;
        } catch (IOException e) {
            log.error("Failed to remove file", e);
            return false;
        }
    }


    /**
     * Sauvegarde un fichier PDF sur le disque et retourne son nom unique.
     *
     * @param file le fichier reçu en Multipart
     * @return le nom unique du fichier sauvegardé
     */
    public String saveFileDocumentPDF(MultipartFile file) {
        // 1. Vérification de nullité
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Le fichier est vide ou nul, impossible de le sauvegarder.");
        }

        // 2. Vérification du nom original
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Le nom du fichier est invalide.");
        }

        // 3. Vérification de l’extension
        String fileExtension = Optional.ofNullable(StringUtils.getFilenameExtension(originalFileName))
                .map(String::toLowerCase)
                .orElse("");
        if (!"pdf".equals(fileExtension)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Le fichier doit être au format PDF");
        }

        // 4. Génération d’un nom unique
        String uniqueFileName =  originalFileName+ "." + fileExtension;

        // 5. Création du chemin de destination
        Path storageDir = Paths.get(basePath).toAbsolutePath().normalize();
        Path targetPath = storageDir.resolve(uniqueFileName);

        try {
            // Créer le répertoire s’il n’existe pas
            Files.createDirectories(storageDir);

            // Sauvegarde avec REPLACE_EXISTING (écrase si même nom)
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Erreur lors de la sauvegarde du fichier : " + originalFileName, e);
        }
    }


    private static String getString(String originalFileName) {
        if (originalFileName == null) {
            throw new IllegalArgumentException("Le nom du fichier est invalide.");
        }

        // Extraire l'extension du fichier
        assert StringUtils.getFilenameExtension(originalFileName) != null;
        String fileExtension = StringUtils.getFilenameExtension(originalFileName).toLowerCase();

        // Vérifier l'extension de fichier avant d'appeler saveFile
        if (!("pdf".equals(fileExtension) )) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Le fichier doit être au format PDF");

        }

        // Générer un nom unique pour le fichier
        String uniqueFileName = originalFileName + "." + fileExtension;
        return uniqueFileName;
    }
}
