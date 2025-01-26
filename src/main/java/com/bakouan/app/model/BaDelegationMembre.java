package com.bakouan.app.model;
import com.bakouan.app.utils.BaUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ba_delegation_membre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaDelegationMembre extends BaAbstractAuditingEntity {
    @Id
    @Column(name = "id")
    private String id = BaUtils.randomUUID();

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "fonction")
    private String fonction;

    @ManyToOne
    @JoinColumn(name = "autorisation_speciale_id")
    private BaAutorisationSpeciale autorisationSpeciale;

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaDocumentPersonnelAutorisationSpecial> documents = new HashSet<>();
}
