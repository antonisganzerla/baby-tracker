package com.sgztech.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgztech.domain.enums.BabySex;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "baby")
public class Baby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 120)
    @NotEmpty(message = "{field.name.required}")
    private String name;

    @Column(name = "birthday")
    @NotNull(message = "{field.birthday.required}")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private BabySex sex;

    @Column(name = "photoUri")
    private String photoUri;

    @Column(name = "registrationDate")
    private LocalDateTime registrationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
