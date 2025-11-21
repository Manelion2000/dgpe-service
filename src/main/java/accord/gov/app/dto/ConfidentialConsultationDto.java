package accord.gov.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class ConfidentialConsultationDto {
    private String username;
    private String ipAdresse;
    private Instant dateConsultation;
    private String action;
    private String sujet;
    private String details;
}
