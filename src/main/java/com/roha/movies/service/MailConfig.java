package com.roha.movies.service;

import io.smallrye.config.ConfigMapping;

/**
 * Created on 26/11/2022.
 */
@ConfigMapping(prefix = "mail")
public interface MailConfig {
    public String host();
    public Integer port();

    public String username();
    public String password();
    public String from();
    public String to();
}