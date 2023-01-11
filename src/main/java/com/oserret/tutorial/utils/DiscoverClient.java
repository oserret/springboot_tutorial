package com.oserret.tutorial.utils;

public class DiscoverClient {

    private String protocol = "http";
    private String host = "localhost";
    private Integer port = 8091;
    private String applicationName = "cogito";
    private String version = "v1";
    private String username = "superadmin";
    private String password = "Cogito";
    private Integer poolMaxConnections = 200;
    private String cartridgeName = "CognizantPoC";
    private String language = "en";

    public DiscoverClient(String protocol,String host, Integer port, String applicationName, String version,
                          String username, String password, Integer poolMaxConnections, String cartridgeName,
                          String language) {

        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.applicationName = applicationName;
        this.version = version;
        this.username = username;
        this.password = password;
        this.poolMaxConnections = poolMaxConnections;
        this.cartridgeName = cartridgeName;
        this.language = language;

    }

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

}
