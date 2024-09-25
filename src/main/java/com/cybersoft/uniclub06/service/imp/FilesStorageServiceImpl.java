package com.cybersoft.uniclub06.service.imp;

import com.cybersoft.uniclub06.exception.FileNotFoundException;
import com.cybersoft.uniclub06.exception.SaveFileException;
import com.cybersoft.uniclub06.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    @Value("${root.path}")
    private String root;

    @Override
    public void save(MultipartFile file) {
        Path rootPath = Paths.get(root);// biến string thành path
        if (!Files.exists(rootPath)) {
            try {
                Files.createDirectories(rootPath);// nếu không có file thì tạo file
            } catch (IOException e) {
                throw new SaveFileException(e.getMessage());
            }
        }
        try {
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            //file upload            resolve là để thêm dấu / vào truyền tên file   Standar là để copy file trùng không bị lỗi
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path rootPath = Paths.get(root);//C:/Users/ACER/Desktop/uploads
            Path file = rootPath.resolve(filename);//C:/Users/ACER/Desktop/uploads/abcseas cái resolve làm
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }

    }


}
