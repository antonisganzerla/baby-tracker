package com.sgztech.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ForgotPasswordDTO {

    @NotEmpty(message = "{field.email.required}")
    @Email(message = "{field.email.must-be-valid}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
