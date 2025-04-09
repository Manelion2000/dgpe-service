package com.bakouan.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaDocumentUploadRequest {
    private BaDocumentDto document;
    @Setter
    @Getter
    private MultipartFile file;

}
