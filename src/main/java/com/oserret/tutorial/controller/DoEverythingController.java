package com.oserret.tutorial.controller;

import ch.qos.logback.classic.Logger;
import com.oserret.tutorial.model.WorkDocument;
import com.oserret.tutorial.service.DocumentParserService;
import com.oserret.tutorial.service.EmailService;
import com.oserret.tutorial.service.StorageService;
import com.oserret.tutorial.service.WorkDocumentProcessorService;
import com.oserret.tutorial.utils.Globals;
import com.oserret.tutorial.utils.OrchestratorProperties;
import com.oserret.tutorial.utils.StorageFileNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DoEverythingController {

    private final StorageService storageService;
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingController.class);
    private final EmailService emailService;
    private OrchestratorProperties orchestratorProperties = null;
    private final DocumentParserService documentParserService;
    private final WorkDocumentProcessorService documentProcessorService;
    private String parsed = "The document hasn't been parsed, error occurred during parsing, please check the log file.";
    private List<WorkDocument> inputRowDocuments = new ArrayList<>();

    private List<WorkDocument> itProjects = new ArrayList<WorkDocument>();
    private List<WorkDocument> ibProjects = new ArrayList<WorkDocument>();
    private List<WorkDocument> ukProjects = new ArrayList<WorkDocument>();
    private List<WorkDocument> frProjects = new ArrayList<WorkDocument>();
    private List<WorkDocument> usProjects = new ArrayList<WorkDocument>();

    public DoEverythingController(StorageService storageService,
                                  EmailService emailService,
                                  OrchestratorProperties orchestratorProperties,
                                  DocumentParserService documentParserService,
                                  WorkDocumentProcessorService documentProcessorService) {
        this.storageService = storageService;
        this.emailService = emailService;
        this.orchestratorProperties = orchestratorProperties;
        this.documentParserService = documentParserService;
        this.documentProcessorService = documentProcessorService;
    }

    @PostMapping("/doAll")
    @ResponseBody
    public String parseDocument(@RequestParam(name = "week", required = false, defaultValue = "1") String week,
                                @RequestParam(name = "format", required = false, defaultValue = Globals.PDF_OUTPUT) String format,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(name = "year", required = false, defaultValue = "2022") String year) {
        logger.info("Call: /upload SERVICE");
        storageService.store(file);
        logger.info("You successfully uploaded the following file: " + file.getOriginalFilename());

        try {
            logger.info("Call: /parseDocument/{filename:.+} SERVICE");
            //storage of the Excel file into the filesystem
            Resource loadedFile = this.storageService.loadAsResource(file.getOriginalFilename());

            Integer intWeek = new Integer(week);
            if (loadedFile.exists() && loadedFile.isFile() && loadedFile.isReadable()) {

                //parse Excel file into multiple documents
                this.inputRowDocuments = this.documentParserService.parseDocument(loadedFile.getFile().toPath());

                portfolioCreation();

                for (String portfolio : this.orchestratorProperties.getPortfolios()) {
                    switch (portfolio) {
                        case Globals.UK_PORTFOLIO:
                            logger.info(Globals.UK_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getUkProjects().size());
                            this.documentProcessorService.processFiles(getUkProjects(), Globals.UK_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.US_PORTFOLIO:
                            logger.info(Globals.US_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getUsProjects().size());
                            this.documentProcessorService.processFiles(getUsProjects(), Globals.US_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.IB_PORTFOLIO:
                            logger.info(Globals.IB_PORTFOLIO + Globals.NUMBER_OF_PROJECTS + getIbProjects().size());
                            this.documentProcessorService.processFiles(getIbProjects(), Globals.IB_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.FR_PORTFOLIO:
                            logger.info(Globals.FR_PORTFOLIO.replace("/", "-") + Globals.NUMBER_OF_PROJECTS + getFrProjects().size());
                            this.documentProcessorService.processFiles(getFrProjects(), Globals.FR_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                        case Globals.IT_PORTFOLIO:
                            logger.info(Globals.IT_PORTFOLIO.replace("/", "-") + Globals.NUMBER_OF_PROJECTS + getItProjects().size());
                            this.documentProcessorService.processFiles(getItProjects(), Globals.IT_PORTFOLIO_SIMPLIFIED, intWeek, this.orchestratorProperties.getOutputLocation(), format);
                            break;
                    }
                }
                try {
                    this.documentProcessorService.zipFolder(Paths.get(this.orchestratorProperties.getOutputLocation()), this.orchestratorProperties.getZipFileName(),this.orchestratorProperties.getOutputZipLocation());
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
        logger.info(this.parsed);

        logger.info("Call: /sendEmail SERVICE");
        try {
            logger.info("****************************");
            logger.info("Properties:");
            logger.info("****************************");
            logger.info("MailSmtpAuth: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpStarttlsEnable: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpHost: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpPort: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpSslTrust: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailUsername: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailPwd: xxxxxxxxxx");

            emailService.sendEmail(orchestratorProperties.getMailSmtpAuth(),
                    orchestratorProperties.getMailSmtpSslProtocols(),
                    orchestratorProperties.getMailSmtpStarttlsEnable(),
                    orchestratorProperties.getMailSmtpHost(),
                    orchestratorProperties.getMailSmtpPort(),
                    orchestratorProperties.getMailSmtpSslTrust(),
                    orchestratorProperties.getMailUsername(),
                    orchestratorProperties.getMailPwd(),
                    orchestratorProperties.getOutputZipLocation() + orchestratorProperties.getZipFileName() + Globals.ZIP_OUTPUT_EXTENSION,
                    orchestratorProperties.getMailForAddress(),
                    week,
                    year);
        } catch (MessagingException |IOException ex) {
            logger.error(ex.getMessage());
        }
        logger.info("Email sent");

        return "Portfolio generation process finished OK";
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
