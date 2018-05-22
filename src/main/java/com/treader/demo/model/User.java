package com.treader.demo.model;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String avatar;
    private String role;
    private String ksid;

    public Integer getId() {
        return id;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String password) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String password) {
        this.role = role;
    }

    public String getKsid() { return ksid; }
    public void setKsid(String ksid) { this.ksid = ksid; }


    public Map getUser() {
        Map user = new HashMap();
        user.put("uid", this.id);
        user.put("name", this.name);
        user.put("email", this.email);
        user.put("avatar", this.avatar);
        return user;
    }




}
