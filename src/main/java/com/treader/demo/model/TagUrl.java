package com.treader.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "tag_url")
public class TagUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer tagId;
    private Integer UrlId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getUrlId() {
        return UrlId;
    }

    public void setUrlId(Integer urlId) {
        UrlId = urlId;
    }
}
