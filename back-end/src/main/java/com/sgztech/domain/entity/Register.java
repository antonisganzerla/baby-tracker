package com.sgztech.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgztech.domain.enums.RegisterSubType;
import com.sgztech.domain.enums.RegisterType;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "register")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "icon")
    @NotNull(message = "{field.icon.required}")
    private Integer icon;

    @Column(name = "name", length = 120)
    @NotEmpty(message = "{field.name.required}")
    private String name;

    @Column(name = "description", length = 120)
    private String description;

    @Column(name = "localDateTime")
    @NotNull(message = "{field.localDateTime.required}")
    private LocalDateTime localDateTime;

    @Column(name = "duration")
    @NotNull(message = "{field.duration.required}")
    private Long duration;

    @Column(name = "note", length = 120)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RegisterType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "subType")
    private RegisterSubType subType;

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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RegisterType getType() {
        return type;
    }

    public void setType(RegisterType type) {
        this.type = type;
    }

    public RegisterSubType getSubType() {
        return subType;
    }

    public void setSubType(RegisterSubType subType) {
        this.subType = subType;
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
