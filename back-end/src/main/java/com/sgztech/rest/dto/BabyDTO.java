package com.sgztech.rest.dto;

import com.sgztech.domain.enums.BabySex;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class BabyDTO {

    private Integer id;

    @NotEmpty(message = "{field.name.required}")
    private String name;

    @NotNull(message = "{field.birthday.required}")
    private LocalDate birthday;

    private BabySex sex;

    private String photoUri;

    @NotNull(message = "{field.userId.required}")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public BabySex getSex() {
        return sex;
    }

    public void setSex(BabySex sex) {
        this.sex = sex;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}