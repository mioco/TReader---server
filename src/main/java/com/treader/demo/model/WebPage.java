package com.treader.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "webpage")
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "text")
    private String html;
    @Column(columnDefinition = "text")
    private String text;

    private String url;

    private Date seen;
}
