package com.oserret.tutorial.service;

import com.oserret.tutorial.model.WorkDocument;
import com.oserret.tutorial.utils.Globals;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DocumentParserServiceImpl implements DocumentParserService {

    private ArrayList<WorkDocument> rowDocuments = new ArrayList<>();

    @Override
    public ArrayList<WorkDocument> parseDocument(Path path) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(path.toFile());
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(1);

            for (Row row : sheet) {
                WorkDocument newRowDocument = new WorkDocument();
                if (row.getRowNum() > 0) {
                    newRowDocument.setRowNumber(row.getRowNum());
                    for (Cell cell : row) {
                        switch (cell.getColumnIndex()) {
                            //week
                            case 0:
                                try {
                                    newRowDocument.setWeek((int) cell.getNumericCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setWeek(Globals.ZERO);
                                }
                                break;
                            //Project Type
                            case 1:
                                try {
                                    newRowDocument.setProjectType(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setProjectType(Globals.NOT_DEFINED);
                                }
                                break;
                            //client_name
                            case 2:
                                try {
                                    newRowDocument.setClientName(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setClientName(Globals.NOT_DEFINED);
                                }
                                break;
                            //portfolio
                            case 3:
                                try {
                                    newRowDocument.setPortfolio(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setPortfolio(Globals.NOT_DEFINED);
                                }
                                break;
                            //Project name
                            case 5:
                                try {
                                    newRowDocument.setProjectName(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setProjectName(Globals.NOT_DEFINED);
                                }
                                break;
                            //Project name
                            case 11:
                                try {
                                    newRowDocument.setPm(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setPm(Globals.NOT_DEFINED);
                                }
                                break;
                            //scope
                            case 33:
                                try {
                                    newRowDocument.setScope(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setScope(Globals.NOT_DEFINED);
                                }
                                break;
                            //Schedule
                            case 34:
                                try {
                                    newRowDocument.setSchedule(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setSchedule(Globals.NOT_DEFINED);
                                }
                                break;
                            //Quality
                            case 35:
                                try {
                                    newRowDocument.setQuality(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setQuality(Globals.NOT_DEFINED);
                                }
                                break;
                            //Customer satisfaction
                            case 36:
                                try {
                                    newRowDocument.setCustomerSatisfaction(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setCustomerSatisfaction(Globals.NOT_DEFINED);
                                }
                                break;
                            //Issues
                            case 37:
                                try {
                                    newRowDocument.setIssues(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setIssues(Globals.NOT_DEFINED);
                                }
                                break;
                            //Planned response
                            case 38:
                                try {
                                    newRowDocument.setPlannedResponse(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    newRowDocument.setPlannedResponse(Globals.NOT_DEFINED);
                                }
                                break;
                            //Due Date
                            case 39:
                                try {
                                    if (cell.getDateCellValue() != null && !cell.getDateCellValue().equals(Globals.VOID_STRING)) {
                                        newRowDocument.setDueDate(cell.getDateCellValue());
                                    } else {
                                        newRowDocument.setDueDate(new Date());
                                    }
                                } catch (Exception ex) {
                                    newRowDocument.setDueDate(new Date());
                                }
                            default:
                                break;
                        }
                    }
                    rowDocuments.add(newRowDocument);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowDocuments;
    }




}
