package com.treader.demo.model;

import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.json.simple.JSONObject;

@Entity // This tells Hibernate to make a table out of this class
public class Users {
    @GenericGenerator(
            name = "userSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "userSequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "userSequenceGenerator")
    private long id;

    private String name;
    private String email;
    private String password;
    private String avatar;
    private String role;
    private String ksid;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(name = "user_url",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "url_id") })
    private Set<Urls> urls = new HashSet<>();

    public long getId() {
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

    public Set<Urls> getUrls() { return urls; }

    public Map getUser() {
        Map user = new HashMap();
        user.put("uid", this.id);
        user.put("name", this.name);
        user.put("email", this.email);
        user.put("avatar", this.avatar);
        user.put("urls", this.urls);

        return user;
    }

    public void removeUrl(Urls url) {
        urls.remove(url);
        url.getUsers().remove(this);
    }


}
