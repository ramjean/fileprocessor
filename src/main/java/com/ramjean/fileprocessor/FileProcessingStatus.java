package com.ramjean.fileprocessor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File processing status should be moved to a database but for this POC it is maintained in a global cache
 */
public class FileProcessingStatus {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessingStatus.class);
    private FileProcessingStatus(){

    } 
    private static Map < String, String > fileList = new HashMap<>();
    /** 
     * 
     */
    static {
        fileList.put("ram-EWNworkstreamAutomationInput", "NEW");
        
    }
    public static FileDetails getFileProcessingStatus(String fileName) {
        logger.info("getFileProcessingStatus : "+ fileList);
        FileDetails fd = new FileDetails();
        fd.setFileName(fileName);
        fd.setProcessingStatus(fileList.get(fileName));
        return fd;
    }   
    public static void setFileProcessingStatus(String key, String value) {
        fileList.put(key, value);
        logger.info("setFileProcessingStatus : "+ fileList);
    }
    public static void setFileProcessingStatus(FileDetails fd) {
        fileList.put(fd.getFileName(), fd.getProcessingStatus());
        logger.info("setFileProcessingStatus : "+ fileList);
    }
}