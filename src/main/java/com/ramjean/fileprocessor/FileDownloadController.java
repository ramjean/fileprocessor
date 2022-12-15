package com.ramjean.fileprocessor;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


import org.springframework.core.io.UrlResource;

@RestController
public class FileDownloadController {
    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
    private static HashMap<String, String> fileList;
    static {
        fileList = new HashMap<String, String>();
        fileList.put("FirstFile", "NEW");
    }

    @GetMapping("/file-processor/file/{file-id}")
    public ResponseEntity<?> downloadFile(@PathVariable("file-id") String fileCode) {

        Resource resource = null;
        try {
            resource = this.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(resource);
    }

    
    @GetMapping("/file-processor/file/{file-id}/status")
    public ResponseEntity<FileDetails> getFileProcessingStatus(@PathVariable("file-id") String fileCode) {
        FileDetails fileDetails = FileProcessingStatus.getFileProcessingStatus(fileCode);
        return new ResponseEntity<>(fileDetails, HttpStatus.OK);
    }


    public Resource getFileAsResource(String fileCode) throws IOException {
        Path foundFile = null;
        Path dirPath = Paths.get("files-repo");
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    if(path.getFileName().toString().startsWith(fileCode)){
                        foundFile = path;
                        break;
                    }
                }
            }
        }
        
        if (foundFile != null) {
            logger.info("foundFile : " + foundFile);
            return new UrlResource(foundFile.toUri());
        }
        
        return null;
        }
}