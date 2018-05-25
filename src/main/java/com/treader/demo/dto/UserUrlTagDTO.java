package com.treader.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserUrlTagDTO {
    private Integer id;
    private String name;
    private String email;

    private String password;
    private String avatar;
    private String role;
    private String ksid;

    private List<UrlTagDTO> urlTagList;

    public List<UrlTagDTO> getUrlTagList() {
        return urlTagList;
    }

    public void setUrlTagList(List<UrlTagDTO> urlTagList) {
        this.urlTagList = urlTagList;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getKsid() {
        return ksid;
    }

    public void setKsid(String ksid) {
        this.ksid = ksid;
    }
}
