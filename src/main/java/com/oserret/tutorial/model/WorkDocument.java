package com.oserret.tutorial.model;

import com.oserret.tutorial.utils.Globals;

import java.util.Date;

public class WorkDocument {
    private String path = Globals.VOID_STRING;
    private Integer week = Globals.ZERO;
    private String projectType = Globals.VOID_STRING;
    private String clientName = Globals.VOID_STRING;
    private String portfolio = Globals.VOID_STRING;
    private String countryClient = Globals.VOID_STRING;
    private String projectName = Globals.VOID_STRING;
    private String wbs = Globals.VOID_STRING;
    private String wbsElement = Globals.VOID_STRING;
    private Integer completionPercentage;
    private String pm = Globals.VOID_STRING;
    private String sales = Globals.VOID_STRING;
    private Date startDate = null;
    private Date plannedEndDate = null;
    private Date actualEndDate = null;
    //Actual Cost
    private float acHours = Globals.ZERO;
    //Actual Cost
    private float acEuro = Globals.ZERO;
    private float hsSoldInContract = Globals.ZERO;
    //budget at completion
    private float bacHours = Globals.ZERO;
    //budget at completion
    private float bacEuros = Globals.ZERO;
    //Estimate to Complete
    private float eacHours = Globals.ZERO;
    //Estimate to Complete
    private float eacEuros = Globals.ZERO;
    //Estimate to Complete
    private float etcHours = Globals.ZERO;
    //Estimate to Complete
    private float etcEuros = Globals.ZERO;
    //Earned value
    private float ev = Globals.ZERO;
    //Cost performance index
    private float cpi = Globals.ZERO;
    private float otherCosts = Globals.ZERO;
    private float revenueAlreadyInvoiced = Globals.ZERO;
    private float orderRevenueTotal = Globals.ZERO;
    private String phase = Globals.VOID_STRING;
    private String subphase = Globals.VOID_STRING;
    private String scope = Globals.VOID_STRING;
    private String schedule = Globals.VOID_STRING;
    private String quality = Globals.VOID_STRING;
    private String customerSatisfaction = Globals.VOID_STRING;
    private String issues = Globals.VOID_STRING;
    private String plannedResponse = Globals.VOID_STRING;
    private Date dueDate = null;
    private int rowNumber = Globals.ZERO;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getCountryClient() {
        return countryClient;
    }

    public void setCountryClient(String countryClient) {
        this.countryClient = countryClient;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    public String getWbsElement() {
        return wbsElement;
    }

    public void setWbsElement(String wbsElement) {
        this.wbsElement = wbsElement;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public float getAcHours() {
        return acHours;
    }

    public void setAcHours(float acHours) {
        this.acHours = acHours;
    }

    public float getAcEuro() {
        return acEuro;
    }

    public void setAcEuro(float acEuro) {
        this.acEuro = acEuro;
    }

    public float getHsSoldInContract() {
        return hsSoldInContract;
    }

    public void setHsSoldInContract(float hsSoldInContract) {
        this.hsSoldInContract = hsSoldInContract;
    }

    public float getBacHours() {
        return bacHours;
    }

    public void setBacHours(float bacHours) {
        this.bacHours = bacHours;
    }

    public float getBacEuros() {
        return bacEuros;
    }

    public void setBacEuros(float bacEuros) {
        this.bacEuros = bacEuros;
    }

    public float getEacHours() {
        return eacHours;
    }

    public void setEacHours(float eacHours) {
        this.eacHours = eacHours;
    }

    public float getEacEuros() {
        return eacEuros;
    }

    public void setEacEuros(float eacEuros) {
        this.eacEuros = eacEuros;
    }

    public float getEtcHours() {
        return etcHours;
    }

    public void setEtcHours(float etcHours) {
        this.etcHours = etcHours;
    }

    public float getEtcEuros() {
        return etcEuros;
    }

    public void setEtcEuros(float etcEuros) {
        this.etcEuros = etcEuros;
    }

    public float getEv() {
        return ev;
    }

    public void setEv(float ev) {
        this.ev = ev;
    }

    public float getCpi() {
        return cpi;
    }

    public void setCpi(float cpi) {
        this.cpi = cpi;
    }

    public float getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(float otherCosts) {
        this.otherCosts = otherCosts;
    }

    public float getRevenueAlreadyInvoiced() {
        return revenueAlreadyInvoiced;
    }

    public void setRevenueAlreadyInvoiced(float revenueAlreadyInvoiced) {
        this.revenueAlreadyInvoiced = revenueAlreadyInvoiced;
    }

    public float getOrderRevenueTotal() {
        return orderRevenueTotal;
    }

    public void setOrderRevenueTotal(float orderRevenueTotal) {
        this.orderRevenueTotal = orderRevenueTotal;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getSubphase() {
        return subphase;
    }

    public void setSubphase(String subphase) {
        this.subphase = subphase;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getCustomerSatisfaction() {
        return customerSatisfaction;
    }

    public void setCustomerSatisfaction(String customerSatisfaction) {
        this.customerSatisfaction = customerSatisfaction;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getPlannedResponse() {
        return plannedResponse;
    }

    public void setPlannedResponse(String plannedResponse) {
        this.plannedResponse = plannedResponse;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
