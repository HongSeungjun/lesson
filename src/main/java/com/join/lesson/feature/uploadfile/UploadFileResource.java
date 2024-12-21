package com.join.lesson.feature.uploadfile;


import com.join.lesson.core.entity.web.UploadFileEntity;
import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploadfile")
public class UploadFileResource {

    private final UploadFileUsecase uploadFileUsecase;

    private final ResourceLoader resourceLoader;

    @PostMapping
    public ApiResponse<String> imageUpload(@Validated @RequestParam("file") MultipartFile file) {
        try {
            UploadFileEntity uploadFile = uploadFileUsecase.store(file);
            return ApiResponse.ok("/api/uploadfile/" + uploadFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId) {
        try {
            UploadFileEntity uploadFile = uploadFileUsecase.load(fileId);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }


}

