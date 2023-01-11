package com.oserret.tutorial.controller;

import ch.qos.logback.classic.Logger;
import com.oserret.tutorial.model.WorkDocument;
import com.oserret.tutorial.service.DocumentParserService;
import com.oserret.tutorial.service.StorageService;
import com.oserret.tutorial.service.WorkDocumentProcessorService;
import com.oserret.tutorial.utils.Globals;
import com.oserret.tutorial.utils.OrchestratorProperties;
import com.oserret.tutorial.utils.StorageFileNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentParserController {

    private String parsed = "The document hasn't been parsed, error occurred during parsing, please check the log file.";
    private List<WorkDocument> inputRowDocuments = new ArrayList<>();
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingController.class);

    private final StorageService storageService;
    private final DocumentParserService documentParserService;
    private final WorkDocumentProcessorService documentProcessorService;
    private final OrchestratorProperties orchestratorProperties;

    private List<WorkDocument> itProjects = new ArrayList<>();
    private List<WorkDocument> ibProjects = new ArrayList<>();
    private List<WorkDocument> ukProjects = new ArrayList<>();
    private List<WorkDocument> frProjects = new ArrayList<>();
    private List<WorkDocument> usProjects = new ArrayList<>();

    @Autowired
    public DocumentParserController(StorageService storageService, DocumentParserService documentParserService, WorkDocumentProcessorService documentProcessorService, OrchestratorProperties properties) {
        this.storageService = storageService;
        this.documentParserService = documentParserService;
        this.documentProcessorService = documentProcessorService;
        this.orchestratorProperties = properties;
    }

    @PostMapping("/parseDocument/{filename:.+}")
    public String parseDocument(@PathVariable String filename, @RequestParam(name = "week", required = false, defaultValue = "1") String week, @RequestParam(name = "format", required = false, defaultValue = Globals.PDF_OUTPUT) String format) {
        try {
            logger.info("Call: /parseDocument/{filename:.+} SERVICE");
            //storage of the Excel file into the filesystem
            Resource file = this.storageService.loadAsResource(filename);

            Integer intWeek = Integer.valueOf(week);
            if (file.exists() && file.isFile() && file.isReadable()) {

                //parse Excel file into multiple documents
                this.inputRowDocuments = this.documentParserService.parseDocument(file.getFile().toPath());

                portfolioCreation();

                for (String portfolio : this.orchestratorProperties.getPortfolios()) {
                    switch (portfolio) {
                        case Globals.UK_PORTFOLIO:
                            System.out.println(Globals.UK_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getUkProjects().size());
                            this.documentProcessorService.processFiles(getUkProjects(), Globals.UK_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.US_PORTFOLIO:
                            System.out.println(Globals.US_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getUsProjects().size());
                            this.documentProcessorService.processFiles(getUsProjects(), Globals.US_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.IB_PORTFOLIO:
                            System.out.println(Globals.IB_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getIbProjects().size());
                            this.documentProcessorService.processFiles(getIbProjects(), Globals.IB_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.FR_PORTFOLIO:
                            System.out.println(Globals.FR_PORTFOLIO.replace("/", "-") + Globals.NUMBER_OF_PROJECTS + getFrProjects().size());
                            this.documentProcessorService.processFiles(getFrProjects(), Globals.FR_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.IT_PORTFOLIO:
                            System.out.println(Globals.IT_PORTFOLIO.replace("/", "-") + Globals.NUMBER_OF_PROJECTS + getItProjects().size());
                            this.documentProcessorService.processFiles(getItProjects(), Globals.IT_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                    }
                }
                try {
                    this.documentProcessorService.zipFolder(Paths.get(this.orchestratorProperties.getOutputLocation()), this.orchestratorProperties.getZipFileName(), orchestratorProperties.getOutputZipLocation());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

                this.inputRowDocuments.clear();
                this.parsed = "The document has been parsed correctly.";
            }
        } catch (StorageFileNotFoundException ex) {
            logger.error(ex.getMessage());
            return "The document is not present on the system. \n" + ex.getMessage();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "Error retrieving the document requested. \n" + e.getMessage();
        }
        return this.parsed;
    }

    @PostMapping("/restart")
    public String restart() {

        storageService.deleteAll();
        storageService.init();
        inputRowDocuments.clear();

        return "All environment restarted";
    }

    private void portfolioCreation() {
        for (WorkDocument inputRowDocument : this.inputRowDocuments) {
            switch (inputRowDocument.getPortfolio()) {
                case Globals.UK_PORTFOLIO:
                    getUkProjects().add(inputRowDocument);
                    break;
                case Globals.US_PORTFOLIO:
                    getUsProjects().add(inputRowDocument);
                    break;
                case Globals.IB_PORTFOLIO:
                    getIbProjects().add(inputRowDocument);
                    break;
                case Globals.FR_PORTFOLIO:
                    getFrProjects().add(inputRowDocument);
                    break;
                case Globals.IT_PORTFOLIO:
                    getItProjects().add(inputRowDocument);
                    break;
            }
        }
    }

    public List<WorkDocument> getItProjects() {
        return itProjects;
    }

    public void setItProjects(List<WorkDocument> itProjects) {
        this.itProjects = itProjects;
    }

    public List<WorkDocument> getIbProjects() {
        return ibProjects;
    }

    public void setIbProjects(List<WorkDocument> ibProjects) {
        this.ibProjects = ibProjects;
    }

    public List<WorkDocument> getUkProjects() {
        return ukProjects;
    }

    public void setUkProjects(List<WorkDocument> ukProjects) {
        this.ukProjects = ukProjects;
    }

    public List<WorkDocument> getFrProjects() {
        return frProjects;
    }

    public void setFrProjects(List<WorkDocument> frProjects) {
        this.frProjects = frProjects;
    }

    public List<WorkDocument> getUsProjects() {
        return usProjects;
    }

    public void setUsProjects(List<WorkDocument> usProjects) {
        this.usProjects = usProjects;
    }
}
