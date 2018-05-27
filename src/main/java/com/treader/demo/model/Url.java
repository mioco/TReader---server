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
    private String domain;
    private String tempItem;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

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
            String[] str1_path = tempItem1.split("/");
            String[] str2_path = tempItem2.split("/");

            StringBuilder newUrl = new StringBuilder();
            for (int i = 0; i < str1_path.length; i++) {
                if (!str1_path[i].equals(str2_path[i])) {
                    newUrl.append(".*");
                } else {
                    newUrl.append(str1_path[i] + "/");
                }
            }
            this.tempItem = newUrl.toString()+"/";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
