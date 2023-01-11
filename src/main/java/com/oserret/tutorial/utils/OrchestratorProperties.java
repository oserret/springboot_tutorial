package com.oserret.tutorial.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("storage")
public class OrchestratorProperties {

    private String location = "C:\\PortfolioGeneratorFolder\\input";
    private String outputLocation = "C:\\PortfolioGeneratorFolder\\output";
    private String outputZipLocation = "C:\\PortfolioGeneratorFolder\\";
    private String zipFileName = "portfolio";
    private String protocol = "http";
    private String host = "localhost";
    private Integer port = 8091;
    private String applicationName = "cogito";
    private String version = "v1";
    private String username = "superadmin";
    private String password = "Cogito";
    private Integer poolMaxConnections = 200;
    private String cartridgeName = "cognizant_AP";
    private String language = "en";

    /*Email configuration properties*/
 /*   private Boolean mailSmtpAuth = true;
    private String mailSmtpStarttlsEnable = "true";
    private String mailSmtpHost = "smtp.gmail.com";
    private String mailSmtpPort = "587";
    private String mailSmtpSslTrust = "smtp.gmail.com";
    private String mailUsername = "cogitoanswers.mailclasify@gmail.com" ;
    private String mailPwd = "ATgYEMLNpcVTS4y" ;
    private String mailForAddress = "oserret@expert.ai";*/

    private Boolean mailSmtpAuth = true;
    private String mailSmtpStarttlsEnable = "true";
    private String mailSmtpHost = "smtp.office365.com";
    private String mailSmtpPort = "587";
    private String mailSmtpSslTrust = "smtp.office365.com";
    private String mailUsername = "PSInternalProcess@expert.ai" ;
    private String mailPwd = "Kox06173" ;
    private String mailForAddress = "psmt@expert.ai";
    //private String mailForAddress = "oserret@expert.ai";
    private String mailSmtpSslProtocols = "TLSv1.2";

    private List<String> portfolios = new ArrayList<>();

    public String getCartridgeName() {
        return cartridgeName;
    }

    public void setCartridgeName(String cartridgeName) {
        this.cartridgeName = cartridgeName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoolMaxConnections() {
        return poolMaxConnections;
    }

    public void setPoolMaxConnections(Integer poolMaxConnections) {
        this.poolMaxConnections = poolMaxConnections;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getPortfolios() {
        portfolios.add(Globals.IT_PORTFOLIO);
        portfolios.add(Globals.FR_PORTFOLIO);
        portfolios.add(Globals.UK_PORTFOLIO);
        portfolios.add(Globals.IB_PORTFOLIO);
        portfolios.add(Globals.US_PORTFOLIO);

        return portfolios;
    }

    public void setPortfolios(List<String> portfolios) {
        portfolios.add(Globals.IT_PORTFOLIO);
        portfolios.add(Globals.FR_PORTFOLIO);
        portfolios.add(Globals.UK_PORTFOLIO);
        portfolios.add(Globals.IB_PORTFOLIO);
        portfolios.add(Globals.US_PORTFOLIO);

        this.portfolios = portfolios;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public Boolean getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(Boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public String getMailSmtpStarttlsEnable() {
        return mailSmtpStarttlsEnable;
    }

    public void setMailSmtpStarttlsEnable(String mailSmtpStarttlsEnable) {
        this.mailSmtpStarttlsEnable = mailSmtpStarttlsEnable;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public String getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(String mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getMailSmtpSslTrust() {
        return mailSmtpSslTrust;
    }

    public void setMailSmtpSslTrust(String mailSmtpSslTrust) {
        this.mailSmtpSslTrust = mailSmtpSslTrust;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPwd() {
        return mailPwd;
    }

    public void setMailPwd(String mailPwd) {
        this.mailPwd = mailPwd;
    }

    public String getMailForAddress() {
        return mailForAddress;
    }

    public void setMailForAddress(String mailForAddress) {
        this.mailForAddress = mailForAddress;
    }

    public String getMailSmtpSslProtocols() {
        return mailSmtpSslProtocols;
    }

    public void setMailSmtpSslProtocols(String mailSmtpSslProtocols) {
        this.mailSmtpSslProtocols = mailSmtpSslProtocols;
    }

    public String getOutputZipLocation() {
        return outputZipLocation;
    }

    public void setOutputZipLocation(String outputZipLocation) {
        this.outputZipLocation = outputZipLocation;
    }
}
