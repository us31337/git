package com.srgcro.feasbase.service;

import org.apache.commons.compress.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class HandleFileAction {

    private final String storagePath;
    private final Logger log = LoggerFactory.getLogger(HandleFileAction.class);

    public HandleFileAction(@Value("${feasbase.storage.path}") String storagePath) {
        this.storagePath = storagePath;
    }

    public String saveFile(MultipartFile file, UUID uuid) {
        String extension = FileNameUtils.getExtension(file.getOriginalFilename());
        File saveFile = new File(storagePath, uuid.toString().concat("." + extension));
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        try {
            Files.copy(file.getInputStream(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("This file cannot be saved");
        }
        return saveFile.getName();
    }
}
