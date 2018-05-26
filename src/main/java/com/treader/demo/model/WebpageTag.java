package com.treader.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "webpage_tag")
public class WebpageTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer webpageId;

    private Integer tagId;
}
