package com.bakouan.app.dto;

import com.bakouan.app.model.BaPhotoPersonnel;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ba_personnel_dgpe")
public class BaPersonneDgpeDto {

    private String id ;
    @NotBlank(message = "le nom et prenom  sont obligatoire")
    private String nomPrenom;

    @NotBlank(message = "la fonction est obligatoire")
    private String fonction;

    private String paragraphe1;

    private String paragraphe2;

    private String paragraphe3;

    private String paragraphe4;
    private Set<BaPhotoPersonnelDto> documents;
}
