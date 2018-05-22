package com.treader.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.net.URL;
import java.util.*;

@Entity
public class Urls {
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

    private String url;
    private String tempItem;
    private String[] keywords;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
        mappedBy = "urls")
    private Set<Users> users = new HashSet();

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String tempItem() { return tempItem; }
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

    public String[] getKeywords() { return this.keywords; }
    public void setKeyWords(String[] keywords) { this.keywords = keywords; }

    public Set<Users> getUsers() { return this.users; }

//    public Set<Tags> getTags() { return tags; }

}
