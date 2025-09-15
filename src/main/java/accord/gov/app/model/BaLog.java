package accord.gov.app.model;

import accord.gov.app.enums.EAction;
import accord.gov.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ba_log")
public class BaLog extends BaAbstractAuditingEntity {

    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "action")
    private EAction action;

    @Column(name = "ip_adresse")
    private String ipAdresse;

    @Column(name = "sujet")
    private String sujet;

    @Column(name = "details")
    private String details;
}
