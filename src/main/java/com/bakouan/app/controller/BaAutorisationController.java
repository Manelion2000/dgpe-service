package com.bakouan.app.controller;

import com.bakouan.app.service.BaAutorisationService;
import com.bakouan.app.service.BaFileStorageService;
import com.bakouan.app.utils.BaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL)
public class BaAutorisationController {
    final BaAutorisationService autorisationService;
    final BaFileStorageService baFileStorageService;
}
