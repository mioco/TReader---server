package com.treader.demo.model;

import javax.persistence.*;
import java.net.URL;

@Entity
@Table(name = "url")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;
    private String tempItem;


    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getTempItem() { return tempItem; }
    public void setTempItem(String tempItem1, String tempItem2) {
        try {
            String[] str1_path = new URL(tempItem1).getPath().split("/");
            String[] str2_path = new URL(tempItem2).getPath().split("/");

            StringBuilder newUrl = new StringBuilder();
            for (int i = 0; i < str1_path.length; i++) {
                if (!str1_path[i].equals(str2_path[i])) {
                    newUrl.append(":*");
                } else {
                    newUrl.append(str1_path[i] + "/");
                }
            }
            this.tempItem = new URL(tempItem1).getHost() + newUrl.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}