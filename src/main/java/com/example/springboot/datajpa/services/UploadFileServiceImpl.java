package com.example.springboot.datajpa.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    private final static String UPLOADS_FOLDER = "uploads";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path path = getPath(filename);
        Resource resource = null;
        resource = new UrlResource(path.toUri());
        if (!resource.exists() && !resource.isReadable()) {
            throw new RuntimeException("Error cannot load image");
        }
        return resource;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        // absolute and external directory
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rootPath = getPath(uniqueFilename);
        Files.copy(file.getInputStream(), rootPath);
        return uniqueFilename;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPath = getPath(filename);
        File archive = rootPath.toFile();

        if (archive.exists() && archive.canRead()) {
            boolean isDelete = archive.delete();
            if (isDelete) {
                System.out.println("Success Delete Photo" + filename);
            }
        }
        return false;
    }

    public Path getPath(String filename) {
        return Path.of(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
