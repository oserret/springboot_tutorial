package com.oserret.tutorial.service;


import com.oserret.tutorial.model.WorkDocument;


import java.nio.file.Path;
import java.util.ArrayList;

public interface DocumentParserService {

    ArrayList<WorkDocument> parseDocument(Path path);

}
