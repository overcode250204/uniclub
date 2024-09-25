package com.cybersoft.uniclub06.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void save(MultipartFile file);
    Resource loadFile(String filename);
}
