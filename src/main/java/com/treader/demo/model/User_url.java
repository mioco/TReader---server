package com.treader.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User_url {
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

    private long urlId;
    private long userId;

    public long getId() {
        return id;
    }

    public long getUrlId() { return urlId; }
    public void setUrlId(long id) { this.urlId = id; }

    public long getUserId() { return userId; }
    public void setUserId(long id) { this.userId = id; }
}
