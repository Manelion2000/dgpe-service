package accord.gov.app.controller;


import accord.gov.app.dto.ConfidentialConsultationDto;
import accord.gov.app.security.BaUserService;
import accord.gov.app.service.BaLogService;
import accord.gov.app.utils.BaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
@CrossOrigin(origins= {"*"})
public class BaLogController {
    private final BaLogService logService;

    /**
     * Liste des utilisateurs ayant consulté les documents confidentiels.
     */
    @GetMapping(BaConstants.URL.LOGS)
    public ResponseEntity<List<ConfidentialConsultationDto>> getConfidentialLogs() {
        return ResponseEntity.ok(logService.getConfidentialConsultations());
    }
}
