package com.oserret.tutorial.service;

import com.oserret.tutorial.model.WorkDocument;
import com.oserret.tutorial.utils.Globals;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class WorkDocumentProcessorServiceImpl implements WorkDocumentProcessorService {

    public PdfFont titleFont;
    public PdfFont regular;
    public PdfFont bold;
    List<WorkDocument> documentsFilteredByWeek_Project = new ArrayList<WorkDocument>();
    List<WorkDocument> documentsFilteredByWeek_Presales = new ArrayList<WorkDocument>();
    List<WorkDocument> documentsFilteredByWeek_Maintenance = new ArrayList<WorkDocument>();
    List<WorkDocument> documentsFilteredByWeek_Financed = new ArrayList<WorkDocument>();
    List<WorkDocument> documentsFilteredByWeek_Internal = new ArrayList<WorkDocument>();
    List<WorkDocument> documentsFilteredByWeek_Poc = new ArrayList<WorkDocument>();

    List<String> headersComplete = new ArrayList<>();
    List<String> headersSpecific = new ArrayList<>();

    @Override
    public PDDocument processFiles(List<WorkDocument> list, String portfolio, String outputFolder) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.beginText();
            contentStream.showText(portfolio);
            contentStream.endText();
            contentStream.close();

            document.save(outputFolder + Globals.SLASH + portfolio + Globals.PDF_OUTPUT_EXTENSION);
            document.close();
        } catch (Exception ex) {

        }
        return document;
    }

    @Override
    public void processFiles(List<WorkDocument> list, String portfolio, Integer week, String outputFolder, String format) {
        switch (format) {
            case Globals.DOCX_OUTPUT:
                processFilesDocx(list, portfolio, week, outputFolder);
                break;
            case Globals.PDF_OUTPUT:
                processFiles(list, portfolio, week, outputFolder);
                break;
        }
    }

    // zip a directory, including sub files and sub directories
    public void zipFolder(Path source, String zipFilename, String outputZipLocation) throws IOException {

        // get folder name as zip file name
        String zipFileName = zipFilename + Globals.ZIP_OUTPUT_EXTENSION;

        try (
                ZipOutputStream zos = new ZipOutputStream(
                        new FileOutputStream(outputZipLocation + zipFileName))
        ) {

            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes) {

                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    try (FileInputStream fis = new FileInputStream(file.toFile())) {

                        Path targetFile = source.relativize(file);
                        zos.putNextEntry(new ZipEntry(targetFile.toString()));

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        // if large file, throws out of memory
                        //byte[] bytes = Files.readAllBytes(file);
                        //zos.write(bytes, 0, bytes.length);

                        zos.closeEntry();

                        System.out.printf("Zip file : %s%n", file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("Unable to zip : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });

        }

    }

    @Override
    public Document processFiles(List<WorkDocument> list, String portfolio, Integer week, String outputFolder) {

        Document doc = null;

        try {

            init();
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputFolder + Globals.SLASH + portfolio.replace(Globals.SLASH, Globals.HYPHEN) + Globals.UNDERSCORE + Globals.PROJECT_PORTFOLIO + week + Globals.PDF_OUTPUT_EXTENSION));
            doc = new Document(pdfDoc);

            this.titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            this.regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            this.bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            Paragraph docTitle = new Paragraph(Globals.PORTFOLIO_TITLE).setMarginRight(1);
            docTitle.setFont(titleFont).setFontSize(16);
            doc.add(docTitle);

            Text portfolioTitle = new Text("Portfolio: ").setFont(bold);
            Text portfolioName = new Text(portfolio).setFont(regular);
            Paragraph portfolioP = new Paragraph().add(portfolioTitle).add(portfolioName).setFontSize(11);
            doc.add(portfolioP);

            Text weekTitle = new Text("Week analyzed: ").setFont(bold);
            Text actualWeek = new Text(week.toString()).setFont(regular);
            Paragraph weekP = new Paragraph().add(weekTitle).add(actualWeek).setFontSize(11);
            doc.add(weekP);

            filterDocuments(list, week);

            if (documentsFilteredByWeek_Project != null && !documentsFilteredByWeek_Project.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Project, Globals.PROJECT_TYPE);
            }
            if (documentsFilteredByWeek_Poc != null && !documentsFilteredByWeek_Poc.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Poc, Globals.POC_TYPE);
            }
            if (documentsFilteredByWeek_Presales != null && !documentsFilteredByWeek_Presales.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Presales, Globals.PRESALES_TYPE);
            }
            if (documentsFilteredByWeek_Maintenance != null && !documentsFilteredByWeek_Maintenance.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Maintenance, Globals.MAINTENANCE_TYPE);
            }
            if (documentsFilteredByWeek_Financed != null && !documentsFilteredByWeek_Financed.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Financed, Globals.FINANCED_TYPE);
            }
            if (documentsFilteredByWeek_Internal != null && !documentsFilteredByWeek_Internal.isEmpty()) {
                doc = generateDocContent(doc, documentsFilteredByWeek_Internal, Globals.INTERNAL_TYPE);
            }

            doc.close();

        } catch (Exception ex) {

        }
        return doc;
    }

    @Override
    public void processFilesDocx(List<WorkDocument> list, String portfolio, Integer week, String outputFolder) {

        try {

            init();
            //Blank Document
            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(outputFolder + Globals.SLASH + portfolio.replace(Globals.SLASH, Globals.HYPHEN) + Globals.UNDERSCORE + Globals.PROJECT_PORTFOLIO + week + Globals.DOCX_OUTPUT_EXTENSION);

            XWPFParagraph docTitle = document.createParagraph();
            XWPFRun portfolioTitle = docTitle.createRun();

            portfolioTitle.setBold(Boolean.TRUE);
            portfolioTitle.setFontSize(16);
            portfolioTitle.setText("Portfolio: ");

            XWPFRun portfolioName = docTitle.createRun();

            portfolioName.setBold(Boolean.FALSE);
            portfolioName.setFontSize(16);
            portfolioName.setText(portfolio);

            XWPFParagraph docWeek = document.createParagraph();
            XWPFRun weekTitle = docWeek.createRun();

            weekTitle.setBold(Boolean.TRUE);
            weekTitle.setFontSize(16);
            weekTitle.setText("Week analyzed: ");

            XWPFRun actualWeek = docWeek.createRun();

            actualWeek.setBold(Boolean.FALSE);
            actualWeek.setFontSize(16);
            actualWeek.setText(week.toString());

            filterDocuments(list, week);

            if (documentsFilteredByWeek_Project != null && !documentsFilteredByWeek_Project.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Project, Globals.PROJECT_TYPE);
            }
            if (documentsFilteredByWeek_Poc != null && !documentsFilteredByWeek_Poc.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Poc, Globals.POC_TYPE);
            }
            if (documentsFilteredByWeek_Presales != null && !documentsFilteredByWeek_Presales.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Presales, Globals.PRESALES_TYPE);
            }
            if (documentsFilteredByWeek_Maintenance != null && !documentsFilteredByWeek_Maintenance.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Maintenance, Globals.MAINTENANCE_TYPE);
            }
            if (documentsFilteredByWeek_Financed != null && !documentsFilteredByWeek_Financed.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Financed, Globals.FINANCED_TYPE);
            }
            if (documentsFilteredByWeek_Internal != null && !documentsFilteredByWeek_Internal.isEmpty()) {
                document = generateDocContent(document, documentsFilteredByWeek_Internal, Globals.INTERNAL_TYPE);
            }

            document.write(out);
            //Close document
            out.close();

        } catch (Exception ex) {

        }
    }

    public Document generateDocContent(Document doc, List<WorkDocument> documents, String type) throws IOException {
        float[] colWidths = {1, 2, 2, 2, 2, 1, 1, 1, 2, 3, 3};
        float[] colWidthsIssuesDocuments = {1, 2, 2, 2, 2, 1, 1, 1, 2};

        Table contentTable = new Table(UnitValue.createPercentArray(colWidths)).setFixedLayout().
                setWidth(UnitValue.createPercentValue(100));

        List<WorkDocument> issuesDocuments = new ArrayList<>();

        Text projectsNumberTitle = new Text("Total number of " + type + " : ").setFont(this.bold);
        Text actualProjectsNumber = new Text(new Integer(documents.size()).toString()).setFont(this.regular);
        Paragraph numberProjectsP = new Paragraph().add(projectsNumberTitle).add(actualProjectsNumber).setFontSize(11);
        doc.add(numberProjectsP);

        contentTable = generateTableHeader(contentTable, headersComplete);

        for (WorkDocument document : documents) {
            if (document != null && ((document.getScope().equals(Globals.GREEN) || document.getScope().equals(Globals.VOID_STRING)) && (document.getSchedule().equals(Globals.GREEN) || document.getSchedule().equals(Globals.VOID_STRING)) && (document.getQuality().equals(Globals.GREEN) || document.getQuality().equals(Globals.VOID_STRING)) && (document.getCustomerSatisfaction().equals(Globals.GREEN) || document.getCustomerSatisfaction().equals(Globals.VOID_STRING)))) {
                contentTable = generateTableContent(contentTable, document);
            } else {
                issuesDocuments.add(document);
            }
        }

        doc.add(contentTable.setHorizontalAlignment(HorizontalAlignment.CENTER));

        Text projectsIssuesNumberTitle = new Text("Total number of " + type + " with issues: ").setFont(this.bold);
        Text actualProjectsIssuesNumber = new Text(new Integer(issuesDocuments.size()).toString()).setFont(this.regular);
        Paragraph numberProjectsIssuesP = new Paragraph().add(projectsIssuesNumberTitle).add(actualProjectsIssuesNumber).setFontSize(11);
        doc.add(numberProjectsIssuesP);

        for (WorkDocument document : issuesDocuments) {
            contentTable = new Table(UnitValue.createPercentArray(colWidthsIssuesDocuments)).setFixedLayout().
                    setWidth(UnitValue.createPercentValue(100));
            contentTable = generateTableHeader(contentTable, headersSpecific);

            contentTable = generateTableContentIssueProject(contentTable, document);
            doc.add(contentTable.setHorizontalAlignment(HorizontalAlignment.CENTER));

            Text pmTitle = new Text("PM: ").setFont(this.bold);
            Text pmText = new Text(document.getPm()).setFont(this.regular);
            Paragraph pmParagraph = new Paragraph().add(pmTitle).add(pmText).setFontSize(8);
            doc.add(pmParagraph);

            Text issueTitle = new Text("Issues: ").setFont(this.bold);
            Text issueText = new Text(document.getIssues()).setFont(this.regular);
            Paragraph issueParagraph = new Paragraph().add(issueTitle).add(issueText).setFontSize(8);
            doc.add(issueParagraph);

            Text plannedResponseTitle = new Text("Planned response (" + document.getDueDate() + ") : ").setFont(this.bold);
            Text plannedResponseText = new Text(document.getPlannedResponse()).setFont(this.regular);
            Paragraph plannedResponseParagraph = new Paragraph().add(plannedResponseTitle).add(plannedResponseText).setFontSize(8);
            doc.add(plannedResponseParagraph);

        }
        return doc;
    }

    public XWPFDocument generateDocContent(XWPFDocument doc, List<WorkDocument> documents, String type) throws IOException {

        List<WorkDocument> issuesDocuments = new ArrayList<>();

        XWPFParagraph docProjectsNumberTitle = doc.createParagraph();
        XWPFRun projectsNumberTitle = docProjectsNumberTitle.createRun();

        projectsNumberTitle.setBold(Boolean.TRUE);
        projectsNumberTitle.setFontSize(16);
        projectsNumberTitle.setText("Total number of " + type + " : ");

        XWPFRun actualProjectsNumber = docProjectsNumberTitle.createRun();

        actualProjectsNumber.setBold(Boolean.FALSE);
        actualProjectsNumber.setFontSize(16);
        actualProjectsNumber.setText(new Integer(documents.size()).toString());


        XWPFTable contentTable = doc.createTable();
        contentTable = generateTableHeader(contentTable, headersSpecific);

        for (WorkDocument document : documents) {
            if (document != null && ((document.getScope().equals(Globals.GREEN) || document.getScope().equals(Globals.VOID_STRING)) && (document.getSchedule().equals(Globals.GREEN) || document.getSchedule().equals(Globals.VOID_STRING)) && (document.getQuality().equals(Globals.GREEN) || document.getQuality().equals(Globals.VOID_STRING)) && (document.getCustomerSatisfaction().equals(Globals.GREEN) || document.getCustomerSatisfaction().equals(Globals.VOID_STRING)))) {
                contentTable = generateTableContentDocx(contentTable, document);
            } else {
                issuesDocuments.add(document);
            }
        }

        XWPFParagraph projectsIssuesNumberTitleParagraph = doc.createParagraph();
        XWPFRun projectsIssuesNumberTitleRun = projectsIssuesNumberTitleParagraph.createRun();

        projectsIssuesNumberTitleRun.setBold(Boolean.TRUE);
        projectsIssuesNumberTitleRun.setFontSize(16);
        projectsIssuesNumberTitleRun.setText("Total number of " + type + " with issues: ");

        XWPFRun actualProjectsIssuesNumberRun = projectsIssuesNumberTitleParagraph.createRun();

        actualProjectsIssuesNumberRun.setBold(Boolean.FALSE);
        actualProjectsIssuesNumberRun.setFontSize(16);
        actualProjectsIssuesNumberRun.setText(new Integer(issuesDocuments.size()).toString());

        for (WorkDocument document : issuesDocuments) {

            XWPFTable contentTableIssues = doc.createTable();
            contentTableIssues = generateTableHeader(contentTableIssues, headersSpecific);

            contentTableIssues = generateTableContentDocx(contentTableIssues, document);

            XWPFParagraph pmTitleParagraph = doc.createParagraph();
            XWPFRun pmTitleRun = pmTitleParagraph.createRun();

            pmTitleRun.setBold(Boolean.TRUE);
            pmTitleRun.setFontSize(11);
            pmTitleRun.setText("PM: ");

            XWPFRun actualPmRun = pmTitleParagraph.createRun();

            actualPmRun.setBold(Boolean.FALSE);
            actualPmRun.setFontSize(11);
            actualPmRun.setText(document.getPm());

            XWPFParagraph issuesTitleParagraph = doc.createParagraph();
            XWPFRun issuesTitleRun = issuesTitleParagraph.createRun();

            issuesTitleRun.setBold(Boolean.TRUE);
            issuesTitleRun.setFontSize(11);
            issuesTitleRun.setText("Issues: ");

            XWPFRun actualIssuesRun = issuesTitleParagraph.createRun();

            actualIssuesRun.setBold(Boolean.FALSE);
            actualIssuesRun.setFontSize(11);
            actualIssuesRun.setText(document.getIssues());

            XWPFParagraph plannedResponseTitleParagraph = doc.createParagraph();
            XWPFRun plannedResponseTitleRun = plannedResponseTitleParagraph.createRun();

            plannedResponseTitleRun.setBold(Boolean.TRUE);
            plannedResponseTitleRun.setFontSize(11);
            plannedResponseTitleRun.setText("Planned response: ");

            XWPFRun actualPlannedResponseRun = plannedResponseTitleParagraph.createRun();

            actualPlannedResponseRun.setBold(Boolean.FALSE);
            actualPlannedResponseRun.setFontSize(11);
            actualPlannedResponseRun.setText(document.getPlannedResponse());

        }

        return doc;
    }


    public Cell generateTableCell(String content, int fontSize, PdfFont font, HorizontalAlignment alignment, Color background) {
        Cell cell = new Cell().add(new Paragraph().add(content).setFont(font).setFontSize(fontSize));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(background);
        return cell;
    }

    public Table generateTableHeader(Table table, List<String> headers) throws IOException {
        for (String header : headers) {
            table.addCell(generateTableCell(header, 6, titleFont, (HorizontalAlignment.CENTER), ColorConstants.LIGHT_GRAY));
        }
        return table;
    }

    public XWPFTable generateTableHeader(XWPFTable table, List<String> headers) throws IOException {
        XWPFTableRow row = table.getRow(0);
        int position = 0;
        for (String header : headers) {
            if (position == 0) {
                row.getCell(position).setText(header);
                row.getCell(position).setColor("D3D3D3");
                row.getCell(position).setWidth("auto");
                position++;
            } else {
                row.addNewTableCell().setText(header);
                row.getCell(position).setColor("D3D3D3");
                row.getCell(position).setWidth("auto");
                position++;
            }
        }
        return table;
    }


    public Table generateTableContent(Table table, WorkDocument document) {
        table.addCell(generateTableCell(document.getWeek().toString(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getProjectType(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getClientName(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getPortfolio(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getProjectName(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getScope(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getScope())));
        table.addCell(generateTableCell(document.getSchedule(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getSchedule())));
        table.addCell(generateTableCell(document.getQuality(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getQuality())));
        table.addCell(generateTableCell(document.getCustomerSatisfaction(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getCustomerSatisfaction())));
        table.addCell(generateTableCell(document.getIssues(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getPlannedResponse(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));

        return table;
    }

    public XWPFTable generateTableContentDocx(XWPFTable table, WorkDocument document) {
        XWPFTableRow row = table.createRow();
        row.getCell(0).setText(document.getWeek().toString());
        row.getCell(1).setText(document.getProjectType().toString());
        row.getCell(2).setText(document.getClientName().toString());
        row.getCell(3).setText(document.getPortfolio().toString());
        row.getCell(4).setText(document.getProjectName().toString());
        row.getCell(5).setText(document.getScope().toString());
        row.getCell(5).setColor(getBackgroundColorRGB(document.getScope()));
        row.getCell(6).setText(document.getSchedule().toString());
        row.getCell(6).setColor(getBackgroundColorRGB(document.getSchedule()));
        row.getCell(7).setText(document.getQuality().toString());
        row.getCell(7).setColor(getBackgroundColorRGB(document.getQuality()));
        row.getCell(8).setText(document.getCustomerSatisfaction().toString());
        row.getCell(8).setColor(getBackgroundColorRGB(document.getCustomerSatisfaction()));
        return table;
    }


    public Table generateTableContentIssueProject(Table table, WorkDocument document) {
        table.addCell(generateTableCell(document.getWeek().toString(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getProjectType(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getClientName(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getPortfolio(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getProjectName(), 6, regular, (HorizontalAlignment.CENTER), ColorConstants.WHITE));
        table.addCell(generateTableCell(document.getScope(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getScope())));
        table.addCell(generateTableCell(document.getSchedule(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getSchedule())));
        table.addCell(generateTableCell(document.getQuality(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getQuality())));
        table.addCell(generateTableCell(document.getCustomerSatisfaction(), 6, regular, (HorizontalAlignment.CENTER), getBackgroundColor(document.getCustomerSatisfaction())));

        return table;
    }

    public XWPFTable generateTableContentIssueProjectDocx(XWPFTable table, WorkDocument document) {
        XWPFTableRow row = table.createRow();
        row.getCell(0).setText(document.getWeek().toString());
        row.getCell(1).setText(document.getProjectType().toString());
        row.getCell(2).setText(document.getClientName().toString());
        row.getCell(3).setText(document.getPortfolio().toString());
        row.getCell(4).setText(document.getProjectName().toString());
        row.getCell(5).setText(document.getScope().toString());
        row.getCell(6).setText(document.getSchedule().toString());
        row.getCell(7).setText(document.getQuality().toString());
        row.getCell(8).setText(document.getCustomerSatisfaction().toString());
        return table;
    }

    public void filterDocuments(List<WorkDocument> list, Integer week) {
        for (WorkDocument document : list) {
            if (document != null && document.getWeek().intValue() == week.intValue()) {
                switch (document.getProjectType()) {
                    case Globals.FINANCED_TYPE:
                        documentsFilteredByWeek_Financed.add(document);
                        break;
                    case Globals.INTERNAL_TYPE:
                        documentsFilteredByWeek_Internal.add(document);
                        break;
                    case Globals.POC_TYPE:
                        documentsFilteredByWeek_Poc.add(document);
                        break;
                    case Globals.PROJECT_TYPE:
                        documentsFilteredByWeek_Project.add(document);
                        break;
                    case Globals.MAINTENANCE_TYPE:
                        documentsFilteredByWeek_Maintenance.add(document);
                        break;
                    case Globals.PRESALES_TYPE:
                        documentsFilteredByWeek_Presales.add(document);
                        break;
                }
            }
        }
    }

    public void init() {

        documentsFilteredByWeek_Project.clear();
        documentsFilteredByWeek_Presales.clear();
        documentsFilteredByWeek_Maintenance.clear();
        documentsFilteredByWeek_Financed.clear();
        documentsFilteredByWeek_Internal.clear();
        documentsFilteredByWeek_Poc.clear();

        headersComplete.clear();
        headersSpecific.clear();

        headersComplete.add("Week");
        headersComplete.add("Project Type");
        headersComplete.add("Client Name");
        headersComplete.add("Portfolio");
        headersComplete.add("Project Name");
        headersComplete.add("Scope");
        headersComplete.add("Schedule");
        headersComplete.add("Quality");
        headersComplete.add("Customer Satisfaction");
        headersComplete.add("Issues");
        headersComplete.add("Planned Response");

        headersSpecific.add("Week");
        headersSpecific.add("Project Type");
        headersSpecific.add("Client Name");
        headersSpecific.add("Portfolio");
        headersSpecific.add("Project Name");
        headersSpecific.add("Scope");
        headersSpecific.add("Schedule");
        headersSpecific.add("Quality");
        headersSpecific.add("Customer Satisfaction");
    }

    public Color getBackgroundColor(String status) {
        Color backgroundColor;

        switch (status) {
            case Globals.GREEN:
                backgroundColor = ColorConstants.GREEN;
                break;
            case Globals.AMBER:
                backgroundColor = ColorConstants.ORANGE;
                break;
            case Globals.RED:
                backgroundColor = ColorConstants.RED;
                break;
            default:
                backgroundColor = ColorConstants.WHITE;
                break;
        }
        return backgroundColor;
    }

    public String getBackgroundColorRGB(String status) {
        String backgroundColor;

        switch (status) {
            case Globals.GREEN:
                backgroundColor = "00FF00";
                break;
            case Globals.AMBER:
                backgroundColor = "ffbf00";
                break;
            case Globals.RED:
                backgroundColor = "FF0000";
                break;
            default:
                backgroundColor = "FFFFFF";
                break;
        }
        return backgroundColor;
    }


}