package com.ramjean.fileprocessor;

public class FileDetails {
    
    private String fileName;
    private String downloadUri;
    private long size;
    private String processingStatus;

    public String getProcessingStatus() {
        return processingStatus;
    }
    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDownloadUri() {
        return downloadUri;
    }
    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }


}
