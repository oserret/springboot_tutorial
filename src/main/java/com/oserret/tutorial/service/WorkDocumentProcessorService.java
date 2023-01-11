package com.oserret.tutorial.service;

import com.oserret.tutorial.model.WorkDocument;
import com.itextpdf.layout.Document;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface WorkDocumentProcessorService {

    PDDocument processFiles(List<WorkDocument> list, String portfolio, String outputFolder);


    Document processFiles(List<WorkDocument> list, String portfolio, Integer week, String outputFolder);
    void processFilesDocx(List<WorkDocument> list, String portfolio, Integer week, String outputFolder);
    void processFiles(List<WorkDocument> list, String portfolio, Integer week, String outputFolder, String format);
    void zipFolder(Path source, String zipFilename, String outputZipLocation) throws IOException;
}
