package com.join.lesson.feature.uploadfile;

import com.join.lesson.core.entity.web.UploadFileEntity;
import com.join.lesson.feature.uploadfile.repository.web.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadFileUsecase {

    private final UploadFileRepository uploadFileRepository;

    private final Path rootLocation = Paths.get("c:/img/");


    public UploadFileEntity store(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = fileSave(rootLocation.toString(), file);
            UploadFileEntity saveFile = UploadFileEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .saveFileName(saveFileName)
                    .contentType(file.getContentType())
                    .size(file.getResource().contentLength())
                    .filePath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName)
                    .build();
            uploadFileRepository.save(saveFile);
            return saveFile;

        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public UploadFileEntity load(Long fileId) {
        return uploadFileRepository.findById(fileId).get();
    }

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }


}
