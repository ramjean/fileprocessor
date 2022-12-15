package com.ramjean.fileprocessor;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @PostMapping("/file-processor/file")
    public ResponseEntity<FileDetails> uploadFile(
        @RequestParam("file") MultipartFile multipartFile)
            throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String fileCode = saveFile(fileName, multipartFile);

        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileName(fileCode);
        fileDetails.setSize(size);
        fileDetails.setDownloadUri("/file-processor/file/" + fileCode);
        fileDetails.setProcessingStatus("NEW");
        
        FileProcessingStatus.setFileProcessingStatus(fileDetails);
        return new ResponseEntity<>(fileDetails, HttpStatus.OK);
    }


    private String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get("files-repo");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
        logger.info("Saved file successfully. fileCode : {fileCode}" + fileCode);
        return fileCode;
    }
}