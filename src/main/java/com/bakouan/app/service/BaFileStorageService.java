package com.bakouan.app.service;

import com.bakouan.app.model.BaDocument;
import com.bakouan.app.model.BaPersonnelDgpe;
import com.bakouan.app.model.BaPhotoPersonnel;
import com.bakouan.app.repositories.BaDocumentRepository;
import com.bakouan.app.repositories.BaPersonnelRepository;
import com.bakouan.app.repositories.BaPhotoPersonnelRepository;
import com.bakouan.app.utils.BaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;

/**
 * Classe de gestion du stockage des fichiers dans le système de fichier.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class BaFileStorageService {

    @Value("${app.storage.path}")
    private String basePath;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private final BaDocumentRepository baDocumentRepository;
    private final BaPersonnelRepository baPersonnelRepository;
    private final BaPhotoPersonnelRepository baPhotoPersonnelRepository;

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
     * Récupérer le contenu d'un fichier.
     *
     * @param idDoc identifiant du fichier.
     * @return un tableau de byte
     */
    public byte[] getDocument(final String idDoc) {

        BaDocument document= baDocumentRepository.findById(idDoc).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Le document introuvable avec id" +idDoc));
        // Construire le chemin complet du fichier
        String fullPath = basePath + File.separator + document.getUrl();
        File file = new File(fullPath);

        try {
            // Vérifier si le fichier existe
            if (file.exists()) {
                log.info("Chargement du fichier : {}", fullPath);
                return Files.readAllBytes(file.toPath());
            } else {
                log.warn("Fichier inexistant au chemin : {}", fullPath);
                throw new FileNotFoundException("Le fichier avec l'ID " + idDoc + " n'existe pas dans le répertoire " + basePath);
            }
        } catch (IOException e) {
            log.error("Erreur lors de la lecture du fichier : " + idDoc, e);
            throw new RuntimeException("Erreur de lecture du fichier " + idDoc, e);
        }
    }

    /**
     * Récupérer le contenu d'un fichier.
     *
     * @param idDoc identifiant du fichier.
     * @return un tableau de byte
     */
    public byte[] getPhotoPersonnel(final String idDoc) {
        BaPhotoPersonnel personnel= baPhotoPersonnelRepository.findById(idDoc).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Le document n'existe pas avec id  " +idDoc));
        // Construire le chemin complet du fichier
        String fullPath = basePath + File.separator + personnel.getUrl();
        File file = new File(fullPath);

        try {
            // Vérifier si le fichier existe
            if (file.exists()) {
                log.info("Chargement du fichier : {}", fullPath);
                return Files.readAllBytes(file.toPath());
            } else {
                log.warn("Fichier inexistant au chemin : {}", fullPath);
                throw new FileNotFoundException("Le fichier avec l'ID " + idDoc + " n'existe pas dans le répertoire " + basePath);
            }
        } catch (IOException e) {
            log.error("Erreur lors de la lecture du fichier : " + idDoc, e);
            throw new RuntimeException("Erreur de lecture du fichier " + idDoc, e);
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

    public String saveFile(MultipartFile file) {
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide et ne peut pas être sauvegardé.");
        }

        // Récupérer le nom du fichier original
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("Le nom du fichier est invalide.");
        }

        // Extraire l'extension du fichier
        String fileExtension = StringUtils.getFilenameExtension(originalFileName).toLowerCase();

        // Vérifier l'extension de fichier avant d'appeler saveFile
        if (!("pdf".equals(fileExtension) || IMAGE_EXTENSIONS.contains(fileExtension))) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Le fichier doit être au format PDF ou une image au format JPG, JPEG, PNG.");

        }
/*// Si c'est une image, vérifier qu'elle est sur fond blanc
        if (IMAGE_EXTENSIONS.contains(fileExtension) && !isWhiteBackgroundImage(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "L'image doit être sur un fond blanc.");
        }*/
        // Générer un nom unique pour le fichier
        String uniqueFileName = BaUtils.randomUUID() + "." + fileExtension;

        // Créer le chemin complet du fichier
        File uploadedFile = new File(basePath + File.separator + uniqueFileName);
        try {
            // Créer le répertoire de stockage s'il n'existe pas
            File storageDirectory = new File(basePath);
            if (!storageDirectory.exists()) {
                storageDirectory.mkdirs();
            }

            // Sauvegarder le fichier
            Files.copy(file.getInputStream(), uploadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier : " + originalFileName, e);
        }
    }

    /**
     * Sauvegarder le contenu d'un fichier.
     *
     * @param file
     * identifiant du fichier.
     * @return un tableau de byte
     */

    public String saveFileDocumentPDF(MultipartFile file) {
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide et ne peut pas être sauvegardé.");
        }

        // Récupérer le nom du fichier original
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("Le nom du fichier est invalide.");
        }

        // Extraire l'extension du fichier
        String fileExtension = StringUtils.getFilenameExtension(originalFileName).toLowerCase();

        // Vérifier l'extension de fichier avant d'appeler saveFile
        if (!("pdf".equals(fileExtension) )) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Le fichier doit être au format PDF");

        }

        // Générer un nom unique pour le fichier
        String uniqueFileName = BaUtils.randomUUID() + "." + fileExtension;

        // Créer le chemin complet du fichier
        File uploadedFile = new File(basePath + File.separator + uniqueFileName);
        try {
            // Créer le répertoire de stockage s'il n'existe pas
            File storageDirectory = new File(basePath);
            if (!storageDirectory.exists()) {
                storageDirectory.mkdirs();
            }

            // Sauvegarder le fichier
            Files.copy(file.getInputStream(), uploadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier : " + originalFileName, e);
        }
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
}
