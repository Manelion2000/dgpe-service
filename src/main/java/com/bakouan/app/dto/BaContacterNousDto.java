package com.bakouan.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaContacterNousDto {
    @NotBlank(message = "le nom et le prenom sont obligatoire")
    private String nomPrenom;

    private String emailOutelephone;

    @NotBlank(message = "le message est obligatoire")
    private String message;
}