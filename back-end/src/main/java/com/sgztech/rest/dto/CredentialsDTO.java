package com.sgztech.rest.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CredentialsDTO {

    @NotEmpty(message = "{field.email.required}")
    @Email(message = "{field.email.must-be-valid}")
    private String email;

    @NotEmpty(message = "{field.password.required}")
    @Length(min = 8, message = "{field.password.must.be.at.least.8.characters}")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}