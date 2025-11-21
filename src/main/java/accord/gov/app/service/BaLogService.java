package accord.gov.app.service;

import accord.gov.app.dto.BaLogDto;
import accord.gov.app.dto.ConfidentialConsultationDto;
import accord.gov.app.mapper.YtMapper;
import accord.gov.app.model.BaLog;
import accord.gov.app.repositories.BaLogRepository;
import accord.gov.app.utils.BaUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BaLogService {

    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);

    /**
     * Utilisé pour avoir accès aux paramètres d'entête
     * d'une requête.
     */
    private final HttpServletRequest request;

    private final BaLogRepository logRepository;

    /**
     * Enregistrer un log.
     *
     * @param logDto
     */
    public void log(final BaLogDto logDto) {
        final BaLog entity = mapper.maps(logDto);
        entity.setIpAdresse(BaUtils.retrieveIP(request));
        this.logRepository.save(entity);
    }

    //=========== GESTION DES LOGS============================
    /**
     * Récupère la liste des consultations de documents confidentiels.
     */
    public List<ConfidentialConsultationDto> getConfidentialConsultations() {

        List<BaLog> logs = logRepository.findConfidentialLogs();

        return logs.stream()
                .map(log -> new ConfidentialConsultationDto(
                        log.getCreatedBy(),
                        log.getIpAdresse(),
                        log.getCreatedDate(),
                        log.getAction().name(),
                        log.getSujet(),
                        log.getDetails()
                ))
                .toList();
    }
}
