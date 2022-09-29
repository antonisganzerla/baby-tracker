package com.sgztech.rest.dto;

import com.sgztech.domain.enums.RegisterSubType;
import com.sgztech.domain.enums.RegisterType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RegisterDTO {

    private Integer id;

    @NotNull(message = "{field.icon.required}")
    private Integer icon;

    @NotEmpty(message = "{field.name.required}")
    private String name;

    private String description;

    @NotNull(message = "{field.localDateTime.required}")
    private LocalDateTime localDateTime;

    @NotNull(message = "{field.duration.required}")
    private Long duration;

    private String note;

    private RegisterType type;

    private RegisterSubType subType;

    @NotNull(message = "{field.userId.required}")
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
