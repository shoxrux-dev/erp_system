package com.example.erp_system.util;

import com.example.erp_system.constant.FilePathConstant;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class FileUtil {

    @SneakyThrows
    public String uploadImage(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] split = originalFilename.split("\\.");
        String fileExtension = split[split.length - 1];
        String fileName = UUID.randomUUID() + "." + fileExtension;
        Path uploadPath = Paths.get(FilePathConstant.FILE_UPLOAD_PATH);
        Path filePath = uploadPath.resolve(fileName);

        Files.createDirectories(uploadPath);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath);
        }

        return fileName;
    }

}
