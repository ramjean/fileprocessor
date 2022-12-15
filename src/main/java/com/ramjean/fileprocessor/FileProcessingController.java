package com.ramjean.fileprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessingController.class);

    @GetMapping("/file-processor/file/{file-id}/print")
    public ResponseEntity<?> printExcelContent(@PathVariable("file-id") String fileName) throws IOException{

        Path dirPath = Paths.get("files-repo");
        String dirPathString = dirPath.getFileName().toAbsolutePath().toString();
        logger.info("Files-Upload : " + dirPathString);
        String fullFileName = "";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    if(path.getFileName().toString().startsWith(fileName)){
                        fullFileName = dirPathString + "/" + path.getFileName().toString();
                        //fullFileName = path.getFileName().toAbsolutePath().toString();
                        break;
                    }
                }
            }
        }
        logger.info("fullFileName : {0}" + fullFileName);
        FileInputStream file = new FileInputStream(new File(fullFileName));
        Workbook workbook = new XSSFWorkbook(file);
        
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING: data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());; break;
                    case NUMERIC: data.get(i).add(cell.getNumericCellValue() + ""); break;
                    case BOOLEAN: data.get(i).add(cell.getBooleanCellValue() + ""); break;
                    case FORMULA: data.get(i).add(cell.getCellFormula() + ""); break;
                    default: data.get(new Integer(i)).add(" ");
                }
            }
            i++;
        }
        System.out.println(data);
        logger.info(data.toString());
        return new ResponseEntity<>(data.toString(), HttpStatus.OK);
    }

}