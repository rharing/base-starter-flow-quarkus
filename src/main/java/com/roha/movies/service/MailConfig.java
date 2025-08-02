package com.roha.movies.service;

import io.smallrye.config.ConfigMapping;

/**
 * Created on 26/11/2022.
 */
@ConfigMapping(prefix = "mail")
public interface MailConfig {
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The hostname of the SMTP server.
     *
     * @return the hostname of the SMTP server
     */
/* <<<<<<<<<<  334b5e4f-6995-4ddd-8f2c-e41a89e2b0e8  >>>>>>>>>>> */
    public String host();
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The port number of the SMTP server.
     *
     * @return the port number of the SMTP server
     */
/* <<<<<<<<<<  cf273bb9-6c4a-4004-9894-881dd63052f4  >>>>>>>>>>> */
    public Integer port();

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The username to use for authentication with the SMTP server.
     *
     * @return the username to use for authentication
     */
/* <<<<<<<<<<  7edb4775-7545-4c3d-a946-0c5016e0a5dc  >>>>>>>>>>> */
    public String username();
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The password to use for authentication with the SMTP server.
     *
     * @return the password to use for authentication
     */
/* <<<<<<<<<<  89b72934-7fed-4858-aad2-433dbfc172f1  >>>>>>>>>>> */
    public String password();
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The email address to use as the sender in the email.
     *
     * @return the sender's email address
     */

/* <<<<<<<<<<  e812bb6f-123e-43f5-9924-55128acd0b3a  >>>>>>>>>>> */
    public String from();
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * The email address to use as the recipient in the email.
     *
     * @return the recipient's email address
     */
    public String to();
}