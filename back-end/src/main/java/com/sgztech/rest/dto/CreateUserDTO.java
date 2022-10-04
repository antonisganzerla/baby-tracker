package com.sgztech.rest.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CreateUserDTO {

    @NotEmpty(message = "{field.name.required}")
    @Length(min = 2, message = "{field.name.must.be.at.least.2.characters}")
    private String name;

    @NotEmpty(message = "{field.email.required}")
    @Email(message = "{field.email.must-be-valid}")
    private String email;

    @NotEmpty(message = "{field.password.required}")
    @Length(min = 8, message = "{field.password.must.be.at.least.8.characters}")
    private String password;

    @NotEmpty(message = "{field.confirm.password.required}")
    @Length(min = 8, message = "{field.confirm.password.must.be.at.least.8.characters}")
    private String confirmPassword;

    private Boolean googleAccount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(Boolean googleAccount) {
        this.googleAccount = googleAccount;
    }
}
