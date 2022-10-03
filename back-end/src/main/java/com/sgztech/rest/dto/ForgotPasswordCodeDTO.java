package com.sgztech.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ForgotPasswordCodeDTO {

    @NotEmpty(message = "{field.email.required}")
    @Email(message = "{field.email.must-be-valid}")
    private String email;

    @NotEmpty(message = "{field.code.required}")
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
